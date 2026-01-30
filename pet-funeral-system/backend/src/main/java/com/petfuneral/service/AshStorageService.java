package com.petfuneral.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petfuneral.common.exception.BusinessException;
import com.petfuneral.dto.AshStorageCreateRequest;
import com.petfuneral.dto.AshStorageDTO;
import com.petfuneral.entity.AshStorage;
import com.petfuneral.entity.Customer;
import com.petfuneral.entity.Pet;
import com.petfuneral.mapper.AshStorageMapper;
import com.petfuneral.mapper.CustomerMapper;
import com.petfuneral.mapper.PetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 骨灰寄存服务
 */
@Service
@RequiredArgsConstructor
public class AshStorageService {

    private final AshStorageMapper ashStorageMapper;
    private final PetMapper petMapper;
    private final CustomerMapper customerMapper;

    /**
     * 分页查询骨灰寄存
     */
    public IPage<AshStorageDTO> getStoragePage(Integer page, Integer size, Integer status, String keyword) {
        Page<AshStorage> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<AshStorage> wrapper = new LambdaQueryWrapper<>();
        
        if (status != null) {
            wrapper.eq(AshStorage::getStatus, status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(AshStorage::getCabinetNo, keyword);
        }
        wrapper.orderByDesc(AshStorage::getCreatedAt);
        
        IPage<AshStorage> result = ashStorageMapper.selectPage(pageParam, wrapper);
        
        // 转换为 DTO
        Page<AshStorageDTO> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        
        return dtoPage;
    }

    /**
     * 获取寄存详情
     */
    public AshStorageDTO getStorageDetail(Long storageId) {
        AshStorage storage = ashStorageMapper.selectById(storageId);
        if (storage == null) {
            throw new BusinessException("寄存记录不存在");
        }
        return convertToDTO(storage);
    }

    /**
     * 获取客户的寄存记录
     */
    public List<AshStorageDTO> getByCustomerId(Long customerId) {
        List<AshStorage> list = ashStorageMapper.selectList(
            new LambdaQueryWrapper<AshStorage>()
                .eq(AshStorage::getCustomerId, customerId)
                .orderByDesc(AshStorage::getCreatedAt)
        );
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 创建骨灰寄存
     */
    @Transactional
    public AshStorage createStorage(AshStorageCreateRequest request) {
        // 检查柜位是否已被占用
        Long count = ashStorageMapper.selectCount(
            new LambdaQueryWrapper<AshStorage>()
                .eq(AshStorage::getCabinetNo, request.getCabinetNo())
                .eq(AshStorage::getStatus, 1) // 寄存中
        );
        if (count > 0) {
            throw new BusinessException("该柜位已被占用");
        }

        AshStorage storage = new AshStorage();
        BeanUtils.copyProperties(request, storage);
        storage.setStatus(1); // 寄存中
        ashStorageMapper.insert(storage);
        return storage;
    }

    /**
     * 续期
     */
    @Transactional
    public void renew(Long storageId, LocalDate newEndDate) {
        AshStorage storage = ashStorageMapper.selectById(storageId);
        if (storage == null) {
            throw new BusinessException("寄存记录不存在");
        }
        if (storage.getStatus() != 1) {
            throw new BusinessException("当前状态不支持续期");
        }
        if (newEndDate.isBefore(storage.getEndDate())) {
            throw new BusinessException("新到期日期不能早于当前到期日期");
        }
        
        storage.setEndDate(newEndDate);
        ashStorageMapper.updateById(storage);
    }

    /**
     * 取走骨灰
     */
    @Transactional
    public void pickup(Long storageId) {
        AshStorage storage = ashStorageMapper.selectById(storageId);
        if (storage == null) {
            throw new BusinessException("寄存记录不存在");
        }
        if (storage.getStatus() != 1) {
            throw new BusinessException("当前状态不支持取走");
        }
        
        storage.setStatus(2); // 已取走
        ashStorageMapper.updateById(storage);
    }

    /**
     * 获取即将到期的寄存记录 (7天内)
     */
    public List<AshStorageDTO> getExpiringStorages(Integer days) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = today.plusDays(days != null ? days : 7);
        
        List<AshStorage> list = ashStorageMapper.selectList(
            new LambdaQueryWrapper<AshStorage>()
                .eq(AshStorage::getStatus, 1) // 寄存中
                .le(AshStorage::getEndDate, deadline)
                .ge(AshStorage::getEndDate, today)
                .orderByAsc(AshStorage::getEndDate)
        );
        
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 获取已过期的寄存记录
     */
    public List<AshStorageDTO> getExpiredStorages() {
        LocalDate today = LocalDate.now();
        
        List<AshStorage> list = ashStorageMapper.selectList(
            new LambdaQueryWrapper<AshStorage>()
                .eq(AshStorage::getStatus, 1) // 寄存中但已过期
                .lt(AshStorage::getEndDate, today)
        );
        
        return list.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 批量更新过期状态
     */
    @Transactional
    public int updateExpiredStatus() {
        LocalDate today = LocalDate.now();
        
        List<AshStorage> expiredList = ashStorageMapper.selectList(
            new LambdaQueryWrapper<AshStorage>()
                .eq(AshStorage::getStatus, 1)
                .lt(AshStorage::getEndDate, today)
        );
        
        for (AshStorage storage : expiredList) {
            storage.setStatus(3); // 已到期
            ashStorageMapper.updateById(storage);
        }
        
        return expiredList.size();
    }

    /**
     * 统计
     */
    public Long countByStatus(Integer status) {
        LambdaQueryWrapper<AshStorage> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(AshStorage::getStatus, status);
        }
        return ashStorageMapper.selectCount(wrapper);
    }

    /**
     * 转换为 DTO
     */
    private AshStorageDTO convertToDTO(AshStorage storage) {
        AshStorageDTO dto = new AshStorageDTO();
        BeanUtils.copyProperties(storage, dto);
        
        // 查询宠物信息
        if (storage.getPetId() != null) {
            Pet pet = petMapper.selectById(storage.getPetId());
            if (pet != null) {
                dto.setPetName(pet.getName());
                dto.setPetBreed(pet.getBreed());
            }
        }
        
        // 查询客户信息
        if (storage.getCustomerId() != null) {
            Customer customer = customerMapper.selectById(storage.getCustomerId());
            if (customer != null) {
                dto.setCustomerName(customer.getNickname());
                dto.setCustomerPhone(customer.getPhone());
            }
        }
        
        // 计算剩余天数
        if (storage.getEndDate() != null && storage.getStatus() == 1) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), storage.getEndDate());
            dto.setDaysRemaining(days);
        }
        
        return dto;
    }
}
