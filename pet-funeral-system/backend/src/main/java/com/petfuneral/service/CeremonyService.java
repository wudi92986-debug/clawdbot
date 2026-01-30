package com.petfuneral.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petfuneral.common.exception.BusinessException;
import com.petfuneral.dto.CeremonyCreateRequest;
import com.petfuneral.dto.CeremonyDTO;
import com.petfuneral.dto.ScheduleDTO;
import com.petfuneral.entity.Ceremony;
import com.petfuneral.entity.Customer;
import com.petfuneral.entity.Pet;
import com.petfuneral.entity.ServiceOrder;
import com.petfuneral.mapper.CeremonyMapper;
import com.petfuneral.mapper.CustomerMapper;
import com.petfuneral.mapper.PetMapper;
import com.petfuneral.mapper.ServiceOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 仪式服务
 */
@Service
@RequiredArgsConstructor
public class CeremonyService {

    private final CeremonyMapper ceremonyMapper;
    private final ServiceOrderMapper orderMapper;
    private final PetMapper petMapper;
    private final CustomerMapper customerMapper;

    private static final String[] CEREMONY_TYPES = {"", "简约告别", "温馨告别", "尊享告别"};
    private static final String[] STATUS_NAMES = {"待安排", "已安排", "进行中", "已完成", "已取消"};

    /**
     * 分页查询仪式
     */
    public IPage<CeremonyDTO> getCeremonyPage(Integer page, Integer size, Integer status, LocalDate date) {
        Page<Ceremony> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Ceremony> wrapper = new LambdaQueryWrapper<>();
        
        if (status != null) {
            wrapper.eq(Ceremony::getStatus, status);
        }
        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            wrapper.between(Ceremony::getStartTime, startOfDay, endOfDay);
        }
        wrapper.orderByDesc(Ceremony::getStartTime);
        
        IPage<Ceremony> result = ceremonyMapper.selectPage(pageParam, wrapper);
        
        Page<CeremonyDTO> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        
        return dtoPage;
    }

    /**
     * 获取仪式详情
     */
    public CeremonyDTO getCeremonyDetail(Long ceremonyId) {
        Ceremony ceremony = ceremonyMapper.selectById(ceremonyId);
        if (ceremony == null) {
            throw new BusinessException("仪式记录不存在");
        }
        return convertToDTO(ceremony);
    }

    /**
     * 创建仪式
     */
    @Transactional
    public Ceremony createCeremony(CeremonyCreateRequest request) {
        // 检查时间冲突
        if (request.getHallNo() != null) {
            checkTimeConflict(request.getHallNo(), request.getStartTime(), request.getEndTime(), null);
        }

        Ceremony ceremony = new Ceremony();
        BeanUtils.copyProperties(request, ceremony);
        ceremony.setStatus(1); // 已安排
        
        // 如果没有设置结束时间，默认1小时
        if (ceremony.getEndTime() == null) {
            ceremony.setEndTime(ceremony.getStartTime().plusHours(1));
        }
        
        ceremonyMapper.insert(ceremony);
        return ceremony;
    }

    /**
     * 更新仪式
     */
    @Transactional
    public void updateCeremony(Long ceremonyId, CeremonyCreateRequest request) {
        Ceremony ceremony = ceremonyMapper.selectById(ceremonyId);
        if (ceremony == null) {
            throw new BusinessException("仪式记录不存在");
        }
        
        // 检查时间冲突
        if (request.getHallNo() != null) {
            checkTimeConflict(request.getHallNo(), request.getStartTime(), request.getEndTime(), ceremonyId);
        }

        BeanUtils.copyProperties(request, ceremony, "id");
        ceremonyMapper.updateById(ceremony);
    }

    /**
     * 开始仪式
     */
    @Transactional
    public void startCeremony(Long ceremonyId) {
        Ceremony ceremony = ceremonyMapper.selectById(ceremonyId);
        if (ceremony == null) {
            throw new BusinessException("仪式记录不存在");
        }
        if (ceremony.getStatus() != 1) {
            throw new BusinessException("当前状态不能开始仪式");
        }
        
        ceremony.setStatus(2); // 进行中
        ceremonyMapper.updateById(ceremony);
    }

    /**
     * 完成仪式
     */
    @Transactional
    public void completeCeremony(Long ceremonyId) {
        Ceremony ceremony = ceremonyMapper.selectById(ceremonyId);
        if (ceremony == null) {
            throw new BusinessException("仪式记录不存在");
        }
        if (ceremony.getStatus() != 2) {
            throw new BusinessException("当前状态不能完成仪式");
        }
        
        ceremony.setStatus(3); // 已完成
        ceremonyMapper.updateById(ceremony);
    }

    /**
     * 取消仪式
     */
    @Transactional
    public void cancelCeremony(Long ceremonyId, String reason) {
        Ceremony ceremony = ceremonyMapper.selectById(ceremonyId);
        if (ceremony == null) {
            throw new BusinessException("仪式记录不存在");
        }
        if (ceremony.getStatus() >= 3) {
            throw new BusinessException("当前状态不能取消");
        }
        
        ceremony.setStatus(4); // 已取消
        ceremony.setRemark(reason);
        ceremonyMapper.updateById(ceremony);
    }

    /**
     * 获取某日的日程
     */
    public List<ScheduleDTO> getDaySchedule(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        List<Ceremony> ceremonies = ceremonyMapper.selectList(
            new LambdaQueryWrapper<Ceremony>()
                .between(Ceremony::getStartTime, startOfDay, endOfDay)
                .ne(Ceremony::getStatus, 4) // 排除已取消
                .orderByAsc(Ceremony::getStartTime)
        );
        
        List<ScheduleDTO> schedules = new ArrayList<>();
        for (Ceremony ceremony : ceremonies) {
            ScheduleDTO schedule = new ScheduleDTO();
            schedule.setId(ceremony.getId());
            schedule.setType("ceremony");
            schedule.setStartTime(ceremony.getStartTime());
            schedule.setEndTime(ceremony.getEndTime());
            schedule.setHallNo(ceremony.getHallNo());
            schedule.setStatus(ceremony.getStatus());
            
            // 设置颜色
            switch (ceremony.getStatus()) {
                case 1 -> schedule.setColor("#409EFF"); // 已安排-蓝色
                case 2 -> schedule.setColor("#E6A23C"); // 进行中-橙色
                case 3 -> schedule.setColor("#67C23A"); // 已完成-绿色
                default -> schedule.setColor("#909399"); // 灰色
            }
            
            // 查询宠物和客户信息
            if (ceremony.getPetId() != null) {
                Pet pet = petMapper.selectById(ceremony.getPetId());
                if (pet != null) {
                    schedule.setPetName(pet.getName());
                    schedule.setTitle(pet.getName() + " - " + CEREMONY_TYPES[ceremony.getCeremonyType()]);
                }
            }
            if (ceremony.getCustomerId() != null) {
                Customer customer = customerMapper.selectById(ceremony.getCustomerId());
                if (customer != null) {
                    schedule.setCustomerName(customer.getNickname());
                }
            }
            
            schedules.add(schedule);
        }
        
        return schedules;
    }

    /**
     * 获取某周的日程
     */
    public List<ScheduleDTO> getWeekSchedule(LocalDate startDate) {
        List<ScheduleDTO> allSchedules = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            allSchedules.addAll(getDaySchedule(startDate.plusDays(i)));
        }
        return allSchedules;
    }

    /**
     * 统计
     */
    public Long countByStatus(Integer status) {
        LambdaQueryWrapper<Ceremony> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Ceremony::getStatus, status);
        }
        return ceremonyMapper.selectCount(wrapper);
    }

    /**
     * 今日待办仪式
     */
    public List<CeremonyDTO> getTodayCeremonies() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        List<Ceremony> list = ceremonyMapper.selectList(
            new LambdaQueryWrapper<Ceremony>()
                .between(Ceremony::getStartTime, startOfDay, endOfDay)
                .in(Ceremony::getStatus, 1, 2) // 已安排或进行中
                .orderByAsc(Ceremony::getStartTime)
        );
        
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 检查时间冲突
     */
    private void checkTimeConflict(String hallNo, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        // 使用 final 变量以便在 lambda 中使用
        final LocalDateTime finalStartTime = startTime;
        final LocalDateTime finalEndTime = (endTime == null) ? startTime.plusHours(1) : endTime;
        
        LambdaQueryWrapper<Ceremony> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ceremony::getHallNo, hallNo)
               .ne(Ceremony::getStatus, 4) // 排除已取消
               .and(w -> w
                   .between(Ceremony::getStartTime, finalStartTime, finalEndTime)
                   .or()
                   .between(Ceremony::getEndTime, finalStartTime, finalEndTime)
                   .or()
                   .apply("start_time <= {0} AND end_time >= {1}", finalStartTime, finalEndTime)
               );
        
        if (excludeId != null) {
            wrapper.ne(Ceremony::getId, excludeId);
        }
        
        Long count = ceremonyMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("该时段仪式厅已被占用");
        }
    }

    /**
     * 转换为 DTO
     */
    private CeremonyDTO convertToDTO(Ceremony ceremony) {
        CeremonyDTO dto = new CeremonyDTO();
        BeanUtils.copyProperties(ceremony, dto);
        
        // 设置类型名称
        if (ceremony.getCeremonyType() != null && ceremony.getCeremonyType() < CEREMONY_TYPES.length) {
            dto.setCeremonyTypeName(CEREMONY_TYPES[ceremony.getCeremonyType()]);
        }
        
        // 设置状态名称
        if (ceremony.getStatus() != null && ceremony.getStatus() < STATUS_NAMES.length) {
            dto.setStatusName(STATUS_NAMES[ceremony.getStatus()]);
        }
        
        // 查询订单信息
        if (ceremony.getOrderId() != null) {
            ServiceOrder order = orderMapper.selectById(ceremony.getOrderId());
            if (order != null) {
                dto.setOrderNo(order.getOrderNo());
            }
        }
        
        // 查询宠物信息
        if (ceremony.getPetId() != null) {
            Pet pet = petMapper.selectById(ceremony.getPetId());
            if (pet != null) {
                dto.setPetName(pet.getName());
                dto.setPetBreed(pet.getBreed());
            }
        }
        
        // 查询客户信息
        if (ceremony.getCustomerId() != null) {
            Customer customer = customerMapper.selectById(ceremony.getCustomerId());
            if (customer != null) {
                dto.setCustomerName(customer.getNickname());
                dto.setCustomerPhone(customer.getPhone());
            }
        }
        
        return dto;
    }
}
