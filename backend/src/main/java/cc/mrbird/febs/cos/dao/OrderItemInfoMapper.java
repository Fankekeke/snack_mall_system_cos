package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderItemInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author FanK
 */
public interface OrderItemInfoMapper extends BaseMapper<OrderItemInfo> {

    /**
     * 查询本月订单明细
     *
     * @param merchantId 商家 ID
     * @return 结果
     */
    List<OrderItemInfo> selectOrderItemsByMonth(@Param("merchantId") Integer merchantId);

    /**
     * 查询本年订单明细
     *
     * @param merchantId 商家 ID
     * @return 结果
     */
    List<OrderItemInfo> selectOrderItemsByYear(@Param("merchantId") Integer merchantId);
}
