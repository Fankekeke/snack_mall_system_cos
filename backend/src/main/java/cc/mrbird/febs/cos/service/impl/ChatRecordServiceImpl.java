package cc.mrbird.febs.cos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.mrbird.febs.cos.dao.ChatRecordMapper;
import cc.mrbird.febs.cos.entity.ChatRecord;
import cc.mrbird.febs.cos.service.IChatRecordService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord> implements IChatRecordService {

    /**
     * 根据车主ID获取沟通联系人列表
     *
     * @param staffId 车主ID
     * @return 联系人列表
     */
    @Override
    public List<LinkedHashMap<String, Object>> getContactsByStaffId(Integer staffId) {
        return baseMapper.getContactsByStaffId(staffId);
    }

    /**
     * 根据用户ID获取沟通联系人列表
     *
     * @param userId 用户ID
     * @return 联系人列表
     */
    @Override
    public List<LinkedHashMap<String, Object>> getContactsByUserId(Integer userId) {
        return baseMapper.getContactsByUserId(userId);
    }
}
