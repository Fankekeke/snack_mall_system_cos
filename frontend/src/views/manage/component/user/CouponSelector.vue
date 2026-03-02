
<template>
  <div class="coupon-selector">
    <a-card title="选择优惠券" :bordered="false">
      <a-row :gutter="12">
        <a-col
          :span="12"
          v-for="(coupon, index) in couponList"
          :key="coupon.id"
        >
          <a-card
            hoverable
            class="coupon-card"
            :class="{ 'selected': selectedCouponId === coupon.id }"
            @click="selectCoupon(coupon)"
          >
            <div slot="title" class="coupon-header">
              <a-badge :status="coupon.status === '0' ? 'success' : 'default'" :text="coupon.status === '0' ? '可用' : '已使用'" />
            </div>

            <div class="coupon-content">
              <!-- 满减券 type=1 -->
              <div v-if="coupon.type == '1'" class="coupon-type">
                <div class="coupon-icon">减</div>
                <div class="coupon-info">
                  <h3>{{ coupon.couponName }}</h3>
                  <p class="discount-detail">满{{ coupon.threshold }}减{{ coupon.discountPrice }}</p>
                  <p class="coupon-desc">{{ coupon.content }}</p>
                </div>
              </div>

              <!-- 折扣券 type=2 -->
              <div v-else-if="coupon.type == '2'" class="coupon-type">
                <div class="coupon-icon">折</div>
                <div class="coupon-info">
                  <h3>{{ coupon.couponName }}</h3>
                  <p class="discount-detail">{{ coupon.rebate }}折优惠</p>
                  <p class="coupon-desc">{{ coupon.content }}</p>
                </div>
              </div>

              <!--              <div class="coupon-validity">-->
              <!--                <a-icon type="clock-circle" />-->
              <!--                有效期至：{{ formatDate(coupon.createDate) }}-->
              <!--              </div>-->
            </div>
          </a-card>
        </a-col>
      </a-row>

      <!-- 已选优惠券信息 -->
      <div v-if="selectedCoupon" class="selected-info">
        <a-alert message="已选择优惠券" type="success" show-icon>
          <template slot="description">
            <strong>{{ selectedCoupon.couponName }}</strong>
            <span v-if="selectedCoupon.type == '1'">
              - 满{{ selectedCoupon.threshold }}减{{ selectedCoupon.discountPrice }}元
            </span>
            <span v-else-if="selectedCoupon.type == '2'">
              - {{ selectedCoupon.rebate }}折优惠
            </span>
            <a-button type="link" size="small" @click="clearSelection">取消选择</a-button>
          </template>
        </a-alert>
      </div>
    </a-card>
  </div>
</template>

<script>export default {
  name: 'CouponSelector',
  props: {
    couponData: {
      type: Array,
      default: () => []
    },
    orderPrice: {
      type: Number,
      default: 0
    }
  },
  data () {
    return {
      couponList: [],
      selectedCouponId: null,
      selectedCoupon: null
    }
  },
  watch: {
    couponData: {
      immediate: true,
      handler (newVal) {
        this.couponList = newVal || []
      }
    }
  },
  methods: {
    selectCoupon (coupon) {
      if (coupon.status !== '0') {
        this.$message.warning('该优惠券已使用，不可选择')
        return
      }

      if (this.selectedCouponId === coupon.id) {
        this.clearSelection()
        return
      }

      console.log(coupon.type)
      if (coupon.type == '1') {
        const threshold = parseFloat(coupon.threshold) || 0
        console.log(threshold)
        console.log(this.orderPrice)
        if (this.orderPrice < threshold) {
          this.$message.warning(`订单金额未达到该优惠券使用门槛（需满${threshold}元）`)
          return
        }
      }

      this.selectedCouponId = coupon.id
      this.selectedCoupon = coupon

      this.$emit('change', {
        couponId: coupon.id,
        couponCode: coupon.code,
        type: coupon.type,
        discountPrice: coupon.type == '1' ? coupon.discountPrice : null,
        rebate: coupon.type == '2' ? coupon.rebate : null,
        threshold: coupon.threshold,
        orderPrice: this.orderPrice
      })

      this.$message.success(`已选择 ${coupon.couponName}`)
    },

    clearSelection () {
      this.selectedCouponId = null
      this.selectedCoupon = null
      this.$emit('change', null)
    },

    formatDate (dateStr) {
      if (!dateStr) return '--'
      return dateStr.split(' ')[0]
    },

    getCouponInfo (coupon) {
      if (coupon.type == '1') {
        return `满${coupon.threshold}减${coupon.discountPrice}`
      } else if (coupon.type == '2') {
        return `${coupon.rebate}折`
      }
      return ''
    }
  }
}
</script>

<style scoped lang="less">

.coupon-card {
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid #f0f0f0;

  &:hover {
    border-color: #1890ff;
    transform: translateY(-4px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  &.selected {
    border-color: #1890ff;
    background-color: #e6f7ff;
  }
}

.coupon-header {
  text-align: right;
}

.coupon-content {
  padding: 8px 0;
}

.coupon-type {
  display: flex;
  align-items: center;
  gap: 16px;
}

.coupon-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  background: linear-gradient(135deg, #ff6b6b, #ee5a6f);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  flex-shrink: 0;
}

.coupon-info {
  flex: 1;

  h3 {
    margin: 0 0 8px 0;
    font-size: 16px;
    color: #333;
  }
}

.discount-detail {
  margin: 0 0 4px 0;
  font-size: 18px;
  font-weight: bold;
  color: #ff4d4f;
}

.coupon-desc {
  margin: 0;
  font-size: 12px;
  color: #999;
}

.coupon-validity {
  margin-top: 12px;
  padding-top: 8px;
  border-top: 1px dashed #e8e8e8;
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 4px;
}

.selected-info {
  margin-top: 24px;
}
</style>
