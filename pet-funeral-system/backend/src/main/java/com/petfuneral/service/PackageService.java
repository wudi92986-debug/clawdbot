package com.petfuneral.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.petfuneral.common.exception.BusinessException;
import com.petfuneral.entity.ServicePackage;
import com.petfuneral.mapper.ServicePackageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务套餐服务
 */
@Service
@RequiredArgsConstructor
public class PackageService {

    private final ServicePackageMapper packageMapper;

    /**
     * 获取所有上架套餐
     */
    public List<ServicePackage> getActivePackages() {
        return packageMapper.selectList(
                new LambdaQueryWrapper<ServicePackage>()
                        .eq(ServicePackage::getStatus, 1)
                        .orderByAsc(ServicePackage::getSortOrder)
        );
    }

    /**
     * 获取所有套餐 (管理端)
     */
    public List<ServicePackage> getAllPackages() {
        return packageMapper.selectList(
                new LambdaQueryWrapper<ServicePackage>()
                        .orderByAsc(ServicePackage::getSortOrder)
        );
    }

    /**
     * 获取套餐详情
     */
    public ServicePackage getPackageDetail(Long packageId) {
        ServicePackage servicePackage = packageMapper.selectById(packageId);
        if (servicePackage == null) {
            throw new BusinessException("套餐不存在");
        }
        return servicePackage;
    }

    /**
     * 创建套餐
     */
    public ServicePackage createPackage(ServicePackage servicePackage) {
        packageMapper.insert(servicePackage);
        return servicePackage;
    }

    /**
     * 更新套餐
     */
    public void updatePackage(ServicePackage servicePackage) {
        packageMapper.updateById(servicePackage);
    }

    /**
     * 上架/下架套餐
     */
    public void toggleStatus(Long packageId) {
        ServicePackage servicePackage = getPackageDetail(packageId);
        servicePackage.setStatus(servicePackage.getStatus() == 1 ? 0 : 1);
        packageMapper.updateById(servicePackage);
    }
}
