package com.petfuneral.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petfuneral.common.exception.BusinessException;
import com.petfuneral.dto.MemorialCreateRequest;
import com.petfuneral.entity.Memorial;
import com.petfuneral.entity.MemorialAlbum;
import com.petfuneral.entity.MemorialMessage;
import com.petfuneral.entity.Pet;
import com.petfuneral.mapper.MemorialAlbumMapper;
import com.petfuneral.mapper.MemorialMapper;
import com.petfuneral.mapper.MemorialMessageMapper;
import com.petfuneral.mapper.PetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 纪念馆服务
 */
@Service
@RequiredArgsConstructor
public class MemorialService {

    private final MemorialMapper memorialMapper;
    private final MemorialMessageMapper messageMapper;
    private final MemorialAlbumMapper albumMapper;
    private final PetMapper petMapper;

    /**
     * 分页查询纪念馆
     */
    public IPage<Memorial> getMemorialPage(Integer page, Integer size, Integer status) {
        Page<Memorial> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Memorial> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Memorial::getStatus, status);
        }
        wrapper.orderByDesc(Memorial::getCreatedAt);
        return memorialMapper.selectPage(pageParam, wrapper);
    }

    /**
     * 获取纪念馆详情
     */
    public Memorial getMemorialDetail(Long memorialId) {
        Memorial memorial = memorialMapper.selectById(memorialId);
        if (memorial == null) {
            throw new BusinessException("纪念馆不存在");
        }
        return memorial;
    }

    /**
     * 通过 URL Key 获取纪念馆
     */
    public Memorial getMemorialByUrlKey(String urlKey) {
        Memorial memorial = memorialMapper.selectOne(
                new LambdaQueryWrapper<Memorial>().eq(Memorial::getUrlKey, urlKey)
        );
        if (memorial == null) {
            throw new BusinessException("纪念馆不存在");
        }
        // 增加访问次数
        memorial.setVisitCount(memorial.getVisitCount() + 1);
        memorialMapper.updateById(memorial);
        return memorial;
    }

    /**
     * 创建纪念馆
     */
    @Transactional
    public Memorial createMemorial(Long customerId, MemorialCreateRequest request) {
        // 验证宠物
        Pet pet = petMapper.selectById(request.getPetId());
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }

        // 检查是否已存在纪念馆
        Long count = memorialMapper.selectCount(
                new LambdaQueryWrapper<Memorial>().eq(Memorial::getPetId, request.getPetId())
        );
        if (count > 0) {
            throw new BusinessException("该宠物已有纪念馆");
        }

        // 生成唯一 URL Key
        String urlKey = IdUtil.fastSimpleUUID().substring(0, 8);

        // 创建纪念馆
        Memorial memorial = new Memorial();
        memorial.setPetId(request.getPetId());
        memorial.setCustomerId(customerId);
        memorial.setUrlKey(urlKey);
        memorial.setTemplateId(request.getTemplateId());
        memorial.setBackgroundMusic(request.getBackgroundMusic());
        memorial.setVisitCount(0);
        memorial.setCandleCount(0);
        memorial.setFlowerCount(0);
        memorial.setPrivacy(request.getPrivacy());
        memorial.setStatus(1);

        memorialMapper.insert(memorial);
        return memorial;
    }

    /**
     * 点烛
     */
    @Transactional
    public void lightCandle(Long memorialId) {
        Memorial memorial = getMemorialDetail(memorialId);
        memorial.setCandleCount(memorial.getCandleCount() + 1);
        memorialMapper.updateById(memorial);
    }

    /**
     * 献花
     */
    @Transactional
    public void offerFlower(Long memorialId) {
        Memorial memorial = getMemorialDetail(memorialId);
        memorial.setFlowerCount(memorial.getFlowerCount() + 1);
        memorialMapper.updateById(memorial);
    }

    /**
     * 添加留言
     */
    @Transactional
    public MemorialMessage addMessage(Long memorialId, Long userId, String nickname, String content) {
        getMemorialDetail(memorialId); // 验证纪念馆存在

        MemorialMessage message = new MemorialMessage();
        message.setMemorialId(memorialId);
        message.setUserId(userId);
        message.setNickname(nickname);
        message.setContent(content);
        message.setCandleCount(0);
        message.setStatus(1);

        messageMapper.insert(message);
        return message;
    }

    /**
     * 获取纪念馆留言列表
     */
    public List<MemorialMessage> getMessages(Long memorialId, Integer limit) {
        LambdaQueryWrapper<MemorialMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemorialMessage::getMemorialId, memorialId)
               .eq(MemorialMessage::getStatus, 1)
               .orderByDesc(MemorialMessage::getCreatedAt);
        if (limit != null) {
            wrapper.last("LIMIT " + limit);
        }
        return messageMapper.selectList(wrapper);
    }

    /**
     * 获取纪念馆相册
     */
    public List<MemorialAlbum> getAlbums(Long memorialId) {
        return albumMapper.selectList(
                new LambdaQueryWrapper<MemorialAlbum>()
                        .eq(MemorialAlbum::getMemorialId, memorialId)
                        .orderByAsc(MemorialAlbum::getSortOrder)
        );
    }

    /**
     * 上传相册照片
     */
    @Transactional
    public MemorialAlbum addAlbumPhoto(Long memorialId, String url, String description) {
        getMemorialDetail(memorialId);

        MemorialAlbum album = new MemorialAlbum();
        album.setMemorialId(memorialId);
        album.setMediaType(1); // 图片
        album.setUrl(url);
        album.setDescription(description);
        album.setSortOrder(0);

        albumMapper.insert(album);
        return album;
    }
}
