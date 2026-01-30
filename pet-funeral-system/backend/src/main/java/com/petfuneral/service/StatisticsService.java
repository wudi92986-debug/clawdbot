package com.petfuneral.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.petfuneral.dto.*;
import com.petfuneral.entity.*;
import com.petfuneral.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计服务
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ServiceOrderMapper orderMapper;
    private final CustomerMapper customerMapper;
    private final PetMapper petMapper;
    private final MemorialMapper memorialMapper;
    private final CeremonyMapper ceremonyMapper;
    private final AshStorageMapper ashStorageMapper;
    private final ServicePackageMapper packageMapper;

    private static final Map<String, String> SPECIES_NAMES = Map.of(
        "dog", "狗",
        "cat", "猫",
        "rabbit", "兔子",
        "bird", "鸟类",
        "other", "其他"
    );

    /**
     * 获取仪表盘统计
     */
    public DashboardStats getDashboardStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();

        // 今日订单和收入
        List<ServiceOrder> todayOrderList = orderMapper.selectList(
            new LambdaQueryWrapper<ServiceOrder>()
                .between(ServiceOrder::getCreatedAt, startOfDay, endOfDay)
                .ne(ServiceOrder::getStatus, 5) // 排除已取消
        );
        Long todayOrders = (long) todayOrderList.size();
        BigDecimal todayRevenue = todayOrderList.stream()
            .map(ServiceOrder::getTotalAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 本月订单和收入
        List<ServiceOrder> monthOrderList = orderMapper.selectList(
            new LambdaQueryWrapper<ServiceOrder>()
                .between(ServiceOrder::getCreatedAt, startOfMonth, endOfDay)
                .ne(ServiceOrder::getStatus, 5)
        );
        Long monthOrders = (long) monthOrderList.size();
        BigDecimal monthRevenue = monthOrderList.stream()
            .map(ServiceOrder::getTotalAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 客户总数
        Long totalCustomers = customerMapper.selectCount(null);

        // 宠物总数
        Long totalPets = petMapper.selectCount(null);

        // 纪念馆总数
        Long totalMemorials = memorialMapper.selectCount(
            new LambdaQueryWrapper<Memorial>().eq(Memorial::getStatus, 1)
        );

        // 待处理订单
        Long pendingOrders = orderMapper.selectCount(
            new LambdaQueryWrapper<ServiceOrder>().in(ServiceOrder::getStatus, 0, 1, 2, 3)
        );

        // 今日仪式
        Long todayCeremonies = ceremonyMapper.selectCount(
            new LambdaQueryWrapper<Ceremony>()
                .between(Ceremony::getStartTime, startOfDay, endOfDay)
                .ne(Ceremony::getStatus, 4)
        );

        // 即将到期寄存 (7天内)
        LocalDate deadline = today.plusDays(7);
        Long expiringStorages = ashStorageMapper.selectCount(
            new LambdaQueryWrapper<AshStorage>()
                .eq(AshStorage::getStatus, 1)
                .le(AshStorage::getEndDate, deadline)
                .ge(AshStorage::getEndDate, today)
        );

        return DashboardStats.builder()
            .todayOrders(todayOrders)
            .todayRevenue(todayRevenue)
            .monthOrders(monthOrders)
            .monthRevenue(monthRevenue)
            .totalCustomers(totalCustomers)
            .totalPets(totalPets)
            .totalMemorials(totalMemorials)
            .pendingOrders(pendingOrders)
            .todayCeremonies(todayCeremonies)
            .expiringStorages(expiringStorages)
            .build();
    }

    /**
     * 获取收入趋势 (按日)
     */
    public List<RevenueStats> getDailyRevenueTrend(Integer days) {
        if (days == null) {
            days = 30;
        }
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        
        // 查询订单
        List<ServiceOrder> orders = orderMapper.selectList(
            new LambdaQueryWrapper<ServiceOrder>()
                .between(ServiceOrder::getCreatedAt, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX))
                .ne(ServiceOrder::getStatus, 5)
        );

        // 按日分组统计
        Map<LocalDate, List<ServiceOrder>> groupedOrders = orders.stream()
            .collect(Collectors.groupingBy(o -> o.getCreatedAt().toLocalDate()));

        List<RevenueStats> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<ServiceOrder> dayOrders = groupedOrders.getOrDefault(date, Collections.emptyList());
            BigDecimal revenue = dayOrders.stream()
                .map(ServiceOrder::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            result.add(new RevenueStats(
                date.format(formatter),
                (long) dayOrders.size(),
                revenue
            ));
        }

        return result;
    }

    /**
     * 获取收入趋势 (按月)
     */
    public List<RevenueStats> getMonthlyRevenueTrend(Integer months) {
        if (months == null) {
            months = 12;
        }
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months - 1).withDayOfMonth(1);
        
        // 查询订单
        List<ServiceOrder> orders = orderMapper.selectList(
            new LambdaQueryWrapper<ServiceOrder>()
                .between(ServiceOrder::getCreatedAt, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX))
                .ne(ServiceOrder::getStatus, 5)
        );

        // 按月分组统计
        Map<String, List<ServiceOrder>> groupedOrders = orders.stream()
            .collect(Collectors.groupingBy(o -> o.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM"))));

        List<RevenueStats> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            String monthKey = current.format(formatter);
            List<ServiceOrder> monthOrders = groupedOrders.getOrDefault(monthKey, Collections.emptyList());
            BigDecimal revenue = monthOrders.stream()
                .map(ServiceOrder::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            result.add(new RevenueStats(
                monthKey,
                (long) monthOrders.size(),
                revenue
            ));
            
            current = current.plusMonths(1);
        }

        return result;
    }

    /**
     * 套餐销售统计
     */
    public List<PackageStats> getPackageSalesStats(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        // 查询套餐
        Map<Long, ServicePackage> packageMap = packageMapper.selectList(null).stream()
            .collect(Collectors.toMap(ServicePackage::getId, p -> p));

        // 查询订单
        List<ServiceOrder> orders = orderMapper.selectList(
            new LambdaQueryWrapper<ServiceOrder>()
                .between(ServiceOrder::getCreatedAt, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX))
                .ne(ServiceOrder::getStatus, 5)
                .isNotNull(ServiceOrder::getPackageId)
        );

        // 按套餐分组统计
        Map<Long, List<ServiceOrder>> groupedOrders = orders.stream()
            .filter(o -> o.getPackageId() != null)
            .collect(Collectors.groupingBy(ServiceOrder::getPackageId));

        BigDecimal totalRevenue = orders.stream()
            .map(ServiceOrder::getTotalAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<PackageStats> result = new ArrayList<>();
        for (Map.Entry<Long, List<ServiceOrder>> entry : groupedOrders.entrySet()) {
            Long packageId = entry.getKey();
            List<ServiceOrder> packageOrders = entry.getValue();
            
            BigDecimal revenue = packageOrders.stream()
                .map(ServiceOrder::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            Double percentage = totalRevenue.compareTo(BigDecimal.ZERO) > 0 
                ? revenue.divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue()
                : 0.0;

            ServicePackage pkg = packageMap.get(packageId);
            result.add(new PackageStats(
                packageId,
                pkg != null ? pkg.getName() : "未知套餐",
                (long) packageOrders.size(),
                revenue,
                percentage
            ));
        }

        // 按收入排序
        result.sort((a, b) -> b.getRevenue().compareTo(a.getRevenue()));
        return result;
    }

    /**
     * 宠物物种统计
     */
    public List<PetSpeciesStats> getPetSpeciesStats() {
        List<Pet> pets = petMapper.selectList(null);
        long total = pets.size();

        Map<String, Long> speciesCount = pets.stream()
            .filter(p -> p.getSpecies() != null)
            .collect(Collectors.groupingBy(Pet::getSpecies, Collectors.counting()));

        List<PetSpeciesStats> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : speciesCount.entrySet()) {
            String species = entry.getKey();
            Long count = entry.getValue();
            Double percentage = total > 0 ? count * 100.0 / total : 0.0;
            
            result.add(new PetSpeciesStats(
                species,
                SPECIES_NAMES.getOrDefault(species, species),
                count,
                percentage
            ));
        }

        // 按数量排序
        result.sort((a, b) -> b.getCount().compareTo(a.getCount()));
        return result;
    }

    /**
     * 订单状态统计
     */
    public Map<String, Long> getOrderStatusStats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("pending", orderMapper.selectCount(
            new LambdaQueryWrapper<ServiceOrder>().eq(ServiceOrder::getStatus, 0)
        ));
        stats.put("confirmed", orderMapper.selectCount(
            new LambdaQueryWrapper<ServiceOrder>().eq(ServiceOrder::getStatus, 1)
        ));
        stats.put("pickup", orderMapper.selectCount(
            new LambdaQueryWrapper<ServiceOrder>().eq(ServiceOrder::getStatus, 2)
        ));
        stats.put("serving", orderMapper.selectCount(
            new LambdaQueryWrapper<ServiceOrder>().eq(ServiceOrder::getStatus, 3)
        ));
        stats.put("completed", orderMapper.selectCount(
            new LambdaQueryWrapper<ServiceOrder>().eq(ServiceOrder::getStatus, 4)
        ));
        stats.put("cancelled", orderMapper.selectCount(
            new LambdaQueryWrapper<ServiceOrder>().eq(ServiceOrder::getStatus, 5)
        ));
        return stats;
    }

    /**
     * 纪念馆互动统计
     */
    public Map<String, Object> getMemorialInteractionStats() {
        List<Memorial> memorials = memorialMapper.selectList(
            new LambdaQueryWrapper<Memorial>().eq(Memorial::getStatus, 1)
        );

        long totalVisits = memorials.stream().mapToLong(Memorial::getVisitCount).sum();
        long totalCandles = memorials.stream().mapToLong(Memorial::getCandleCount).sum();
        long totalFlowers = memorials.stream().mapToLong(Memorial::getFlowerCount).sum();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalMemorials", memorials.size());
        stats.put("totalVisits", totalVisits);
        stats.put("totalCandles", totalCandles);
        stats.put("totalFlowers", totalFlowers);
        stats.put("avgVisitsPerMemorial", memorials.isEmpty() ? 0 : totalVisits / memorials.size());

        return stats;
    }
}
