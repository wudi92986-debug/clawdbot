package com.petfuneral.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petfuneral.common.exception.BusinessException;
import com.petfuneral.dto.PetCreateRequest;
import com.petfuneral.dto.PetDeathRequest;
import com.petfuneral.entity.Pet;
import com.petfuneral.mapper.PetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 宠物服务
 */
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetMapper petMapper;

    /**
     * 分页查询宠物
     */
    public IPage<Pet> getPetPage(Integer page, Integer size, String keyword, Integer status, Long customerId) {
        Page<Pet> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w
                .like(Pet::getName, keyword)
                .or()
                .like(Pet::getBreed, keyword)
            );
        }
        if (status != null) {
            wrapper.eq(Pet::getStatus, status);
        }
        if (customerId != null) {
            wrapper.eq(Pet::getCustomerId, customerId);
        }
        wrapper.orderByDesc(Pet::getCreatedAt);
        
        return petMapper.selectPage(pageParam, wrapper);
    }

    /**
     * 获取宠物详情
     */
    public Pet getPetDetail(Long petId) {
        Pet pet = petMapper.selectById(petId);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }
        return pet;
    }

    /**
     * 获取客户的所有宠物
     */
    public List<Pet> getByCustomerId(Long customerId) {
        return petMapper.selectList(
            new LambdaQueryWrapper<Pet>()
                .eq(Pet::getCustomerId, customerId)
                .orderByDesc(Pet::getCreatedAt)
        );
    }

    /**
     * 创建宠物
     */
    @Transactional
    public Pet createPet(PetCreateRequest request) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(request, pet);
        pet.setStatus(1); // 在世
        petMapper.insert(pet);
        return pet;
    }

    /**
     * 更新宠物信息
     */
    @Transactional
    public void updatePet(Long petId, PetCreateRequest request) {
        Pet pet = getPetDetail(petId);
        BeanUtils.copyProperties(request, pet, "id", "customerId", "status", "deathDate", "deathReason");
        petMapper.updateById(pet);
    }

    /**
     * 登记宠物离世
     */
    @Transactional
    public void registerDeath(Long petId, PetDeathRequest request) {
        Pet pet = getPetDetail(petId);
        if (pet.getStatus() == 2) {
            throw new BusinessException("该宠物已登记离世");
        }
        
        pet.setStatus(2); // 已离世
        pet.setDeathDate(request.getDeathDate());
        pet.setDeathReason(request.getDeathReason());
        petMapper.updateById(pet);
    }

    /**
     * 删除宠物
     */
    @Transactional
    public void deletePet(Long petId) {
        Pet pet = getPetDetail(petId);
        petMapper.deleteById(petId);
    }

    /**
     * 统计宠物数量
     */
    public Long count() {
        return petMapper.selectCount(null);
    }

    /**
     * 按状态统计
     */
    public Long countByStatus(Integer status) {
        return petMapper.selectCount(
            new LambdaQueryWrapper<Pet>().eq(Pet::getStatus, status)
        );
    }

    /**
     * 按物种统计
     */
    public List<Pet> getSpeciesStats() {
        // 简单返回，实际可用 SQL 分组统计
        return petMapper.selectList(null);
    }
}
