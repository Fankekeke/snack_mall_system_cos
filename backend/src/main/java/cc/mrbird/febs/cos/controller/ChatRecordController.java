package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MerchantInfo;
import cc.mrbird.febs.cos.service.IMerchantInfoService;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cc.mrbird.febs.cos.entity.ChatRecord;
import cc.mrbird.febs.cos.entity.StaffInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IChatRecordService;
import cc.mrbird.febs.cos.service.IStaffInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/business/chat-record")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChatRecordController {

    private final IChatRecordService chatRecordService;

    private final IUserInfoService userInfoService;

    private final IMerchantInfoService merchantInfoService;

    @Resource
    private Generation generation;

    /**
     * 分页查询聊天记录
     *
     * @param page       分页对象
     * @param chatRecord 聊天记录
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ChatRecord> page, ChatRecord chatRecord) {
        return R.ok(chatRecordService.page(page, Wrappers.<ChatRecord>lambdaQuery()
                .eq(chatRecord.getUserId() != null, ChatRecord::getUserId, chatRecord.getUserId())
                .eq(chatRecord.getStaffId() != null, ChatRecord::getStaffId, chatRecord.getStaffId())
                .orderByDesc(ChatRecord::getCreateTime)));
    }

    /**
     * 根据商家ID获取沟通联系人列表
     *
     * @param staffId 商家ID
     * @return 联系人列表
     */
    @GetMapping("/contacts/staff/{staffId}")
    public R getContactsByStaffId(@PathVariable Integer staffId) {
        MerchantInfo staffInfo = merchantInfoService.getOne(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getUserId, staffId));
        return R.ok(chatRecordService.getContactsByStaffId(staffInfo.getId()));
    }

    /**
     * 根据用户ID获取沟通联系人列表
     *
     * @param userId 用户ID
     * @return 联系人列表
     */
    @GetMapping("/contacts/user/{userId}")
    public R
    getContactsByUserId(@PathVariable Integer userId) {
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        return R.ok(chatRecordService.getContactsByUserId(userInfo.getId()));
    }

    /**
     * 根据用户ID和商家ID获取聊天记录
     *
     * @param userId  用户ID
     * @param staffId 商家ID
     * @return 结果
     */
    @GetMapping("/list")
    public R getListByUserAndStaff(@RequestParam Integer userId, @RequestParam Integer staffId) {
        List<ChatRecord> list = chatRecordService.list(Wrappers.<ChatRecord>lambdaQuery()
                .eq(ChatRecord::getUserId, userId)
                .eq(ChatRecord::getStaffId, staffId)
                .orderByAsc(ChatRecord::getCreateTime));
        return R.ok(list);
    }

    /**
     * 发送消息
     *
     * @param chatRecord 聊天记录
     * @return 结果
     */
    @PostMapping
    public R sendMsg(ChatRecord chatRecord) throws NoApiKeyException, InputRequiredException {
        chatRecord.setCreateTime(DateUtil.formatDateTime(new Date()));

        String result = send(chatRecord.getContent() + "，总是给出简洁的回答");
        ChatRecord chatRecord1 = new ChatRecord();
        chatRecord1.setUserId(chatRecord.getUserId());
        chatRecord1.setStaffId(chatRecord.getStaffId());
        chatRecord.setCreateTime(DateUtil.formatDateTime(new Date()));
        chatRecord1.setContent(result);
        chatRecord1.setSenderType("1");
        chatRecord1.setStatus("0");
        chatRecord1.setIsDeleted(false);
        chatRecordService.save(chatRecord1);

        return R.ok(chatRecordService.save(chatRecord));
    }

    public String send(String content) throws NoApiKeyException, InputRequiredException {
        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(content)
                .build();

        GenerationParam param = GenerationParam.builder()
                //指定用于对话的通义千问模型名
                .model("qwen-plus")
                .messages(Arrays.asList(userMessage))
                //
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                //生成过程中核采样方法概率阈值，例如，取值为0.8时，仅保留概率加起来大于等于0.8的最可能token的最小集合作为候选集。
                // 取值范围为（0,1.0)，取值越大，生成的随机性越高；取值越低，生成的确定性越高。
                .topP(0.8)
                //阿里云控制台DASHSCOPE获取的api-key
                .apiKey("sk-eeb84a761e0941ad8b8bc9519d419dc0")
                //启用互联网搜索，模型会将搜索结果作为文本生成过程中的参考信息，但模型会基于其内部逻辑“自行判断”是否使用互联网搜索结果。
                .enableSearch(true)
                .build();
        GenerationResult generationResult = generation.call(param);
//        String json = generationResult.getOutput().getChoices().get(0).getMessage().getContent();
        // 获取所有 content 内容并放入 List 中
        List<String> allContents = generationResult.getOutput().getChoices().stream()
                .map(choice -> choice.getMessage().getContent())
                .collect(Collectors.toList());

        String combinedContent = String.join("\n---\n", allContents); // 使用 "---" 分隔多个回复内容
        return combinedContent;
    }

    /**
     * 发送消息
     *
     * @param chatRecord 聊天记录
     * @return 结果
     */
    @PostMapping("/defaultChat")
    public R defaultChat(ChatRecord chatRecord) {
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, chatRecord.getUserId()));
        chatRecord.setUserId(userInfo.getId());
        chatRecord.setCreateTime(DateUtil.formatDateTime(new Date()));
        return R.ok(chatRecordService.save(chatRecord));
    }

    /**
     * 发送消息
     *
     * @param chatRecord 聊天记录
     * @return 结果
     */
    @PostMapping("/defaultStaffChat")
    public R defaultStaffChat(ChatRecord chatRecord) {
        MerchantInfo staffInfo = merchantInfoService.getOne(Wrappers.<MerchantInfo>lambdaQuery().eq(MerchantInfo::getUserId, chatRecord.getStaffId()));
        chatRecord.setStaffId(staffInfo.getId());
        chatRecord.setCreateTime(DateUtil.formatDateTime(new Date()));
        return R.ok(chatRecordService.save(chatRecord));
    }

    /**
     * 标记消息为已读
     *
     * @param id 消息ID
     * @return 结果
     */
    @PutMapping("/read/{id}")
    public R markAsRead(@PathVariable Integer id) {
        ChatRecord chatRecord = new ChatRecord();
        chatRecord.setId(id);
        chatRecord.setStatus("1");
        return R.ok(chatRecordService.updateById(chatRecord));
    }
}
