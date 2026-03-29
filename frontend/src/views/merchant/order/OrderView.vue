<template>
  <a-modal v-model="show" title="订单详情" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单信息</span></a-col>
        <a-col :span="6"><b>订单编号：</b>
          {{ orderInfo.code }}
        </a-col>
        <a-col :span="6"><b>订单价格：</b>
          {{ orderInfo.orderPrice ? orderInfo.orderPrice + '元' : '- -' }}
        </a-col>
        <a-col :span="6"><b>折后价格：</b>
          {{ orderInfo.afterOrderPrice ? orderInfo.afterOrderPrice + '元' : '- -' }}
        </a-col>
        <a-col :span="6"><b>会员折扣：</b>
          {{ orderInfo.discount }} 元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>订单积分：</b>
          {{ orderInfo.integral }}
        </a-col>
        <a-col :span="6"><b>订单状态：</b>
          <span v-if="orderInfo.status === '0'" style="color: red">未支付</span>
          <span v-if="orderInfo.status === '1'" style="color: blue">已支付</span>
          <span v-if="orderInfo.status === '2'" style="color: orange">配送中</span>
          <span v-if="orderInfo.status === '3'" style="color: green">已收货</span>
        </a-col>
        <a-col :span="6"><b>订单类型：</b>
          <span v-if="orderInfo.type === '0'">自取</span>
          <span v-if="orderInfo.type === '1'">外送</span>
        </a-col>
        <a-col :span="6"><b>下单时间：</b>
          {{ orderInfo.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" v-if="orderInfo.type === '1'">
        <a-col :span="6"><b>公里数：</b>
          {{ orderInfo.kilometre }}公里
        </a-col>
        <a-col :span="6"><b>配送价格：</b>
          {{ orderInfo.distributionPrice ? orderInfo.distributionPrice + '元' : '- -' }}
        </a-col>
        <a-col :span="6"><b>支付时间：</b>
          {{ orderInfo.payDate ? orderInfo.payDate : '- -' }}
        </a-col>
        <a-col :span="6"><b>送达时间：</b>
          {{ orderInfo.serviceDate }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="userInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">用户信息</span></a-col>
        <a-col :span="6"><b>会员编号：</b>
          {{ userInfo.code }}
        </a-col>
        <a-col :span="6"><b>用户姓名：</b>
          {{ userInfo.name ? userInfo.name : '- -' }}
        </a-col>
        <a-col :span="6"><b>邮箱地址：</b>
          {{ userInfo.mail ? userInfo.mail : '- -' }}
        </a-col>
        <a-col :span="6"><b>联系电话：</b>
          {{ userInfo.phone }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="merchantInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">商家信息</span></a-col>
        <a-col :span="6"><b>商家编号：</b>
          {{ merchantInfo.code }}
        </a-col>
        <a-col :span="6"><b>商家名称：</b>
          {{ merchantInfo.name ? merchantInfo.name : '- -' }}
        </a-col>
        <a-col :span="6"><b>地 址：</b>
          {{ merchantInfo.address ? merchantInfo.address : '- -' }}
        </a-col>
        <a-col :span="6"><b>负责人：</b>
          {{ merchantInfo.principal }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="6"><b>联系方式：</b>
          {{ merchantInfo.phone }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderItemInfo.length !== 0">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买商品</span></a-col>
        <a-table :columns="columns" :data-source="orderItemInfo"></a-table>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="addressInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">收货地址</span></a-col>
        <a-col :span="6"><b>收货编号：</b>
          {{ addressInfo.code }}
        </a-col>
        <a-col :span="6"><b>详细地址：</b>
          {{ addressInfo.address ? addressInfo.address : '- -' }}
        </a-col>
        <a-col :span="6"><b>联系人：</b>
          {{ addressInfo.contactPerson ? addressInfo.contactPerson : '- -' }}
        </a-col>
        <a-col :span="6"><b>联系方式：</b>
          {{ addressInfo.contactMethod }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderData.status !== null && orderData.status == '2'">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px;display: flex; justify-content: space-between; align-items: center;">
          <span style="font-size: 15px;font-weight: 650;color: #000c17">物流更新</span>
          <a-button type="primary" size="small" @click="showLogisticsModal">
            <a-icon type="plus" />
            更新物流
          </a-button>
        </a-col>
        <a-col :span="24">
          <a-timeline>
            <a-timeline-item
              v-for="(logistics, index) in logisticsList"
              :key="index"
              :color="index === 0 ? 'green' : 'blue'"
            >
              <template slot="dot">
                <a-icon v-if="index === 0" type="check-circle" style="font-size: 16px; color: #52c41a;" />
                <a-icon v-else type="clock-circle" style="font-size: 16px; color: #1890ff;" />
              </template>
              <div style="font-size: 13px; font-family: SimHei;">
                <p style="margin: 0 0 5px 0; font-weight: bold;">{{ logistics.time }}</p>
                <p style="margin: 0; color: #666;">{{ logistics.content }}</p>
              </div>
            </a-timeline-item>
          </a-timeline>
          <div v-if="!logisticsList || logisticsList.length === 0" style="text-align: center; padding: 30px; color: #999;">
            <a-icon type="info-circle" style="font-size: 24px; margin-bottom: 10px;" />
            <p>暂无物流信息，请点击右上角按钮添加</p>
          </div>
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="staffInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">配送员信息</span></a-col>
        <a-col :span="6"><b>员工姓名：</b>
          {{ staffInfo.name }}
        </a-col>
        <a-col :span="6"><b>性别：</b>
          <span v-if="orderInfo.type === '1'">男</span>
          <span v-if="orderInfo.type === '2'">女</span>
        </a-col>
        <a-col :span="6"><b>员工工号：</b>
          {{ staffInfo.code }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <br/>
    <div style="font-size: 13px;font-family: SimHei" v-if="evaluateInfo !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单评价</span></a-col>
        <a-col :span="6"><b>评价分数：</b>
          <a-rate :default-value="evaluateInfo.score" disabled />
        </a-col>
        <a-col :span="6"><b>评价内容：</b>
          <a-tooltip>
            <template slot="title">
              {{ evaluateInfo.content}}
            </template>
            {{ evaluateInfo.content.slice(0, 8) }} ...
          </a-tooltip>
        </a-col>
        <a-col :span="6"><b>评价时间：</b>
          {{ evaluateInfo.createDate ? evaluateInfo.createDate : '- -' }}
        </a-col>
      </a-row>
      <br/>
    </div>
    <a-modal
      v-model="logisticsModalVisible"
      title="更新物流信息"
      @cancel="handleLogisticsCancel"
      :width="600"
    >
      <a-form-model
        ref="logisticsForm"
        :model="logisticsForm"
        :rules="logisticsRules"
        layout="vertical"
      >
        <a-form-model-item label="物流时间" prop="time">
          <a-date-picker
            v-model="logisticsForm.time"
            show-time
            format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择物流更新时间"          style="width: 100%"
          />
        </a-form-model-item>
        <a-form-model-item label="物流内容" prop="content">
          <a-textarea
            v-model="logisticsForm.content"
            placeholder="请输入物流状态，例如：已发货、运输中、已签收等"
            :rows="4"
          />
        </a-form-model-item>
      </a-form-model>

      <template slot="footer">
        <a-button key="back" @click="handleLogisticsCancel">
          取消
        </a-button>
        <a-button key="submit" type="primary" :loading="submitting" @click="submitLogistics">
          确定
        </a-button>
      </template>
    </a-modal>
    <br/>
  </a-modal>
</template>

<script>
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'orderView',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.orderShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '商品名称',
        dataIndex: 'dishesName'
      }, {
        title: '图片',
        dataIndex: 'images',
        customRender: (text, record, index) => {
          if (!record.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '购买数量',
        dataIndex: 'amount'
      }, {
        title: '单价',
        dataIndex: 'unitPrice'
      }, {
        title: '总价格',
        dataIndex: 'totalPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }]
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      repairInfo: null,
      reserveInfo: null,
      durgList: [],
      logisticsList: [],
      current: 0,
      userInfo: null,
      orderInfo: null,
      merchantInfo: null,
      orderItemInfo: [],
      addressInfo: null,
      staffInfo: null,
      evaluateInfo: null,
      logisticsModalVisible: false,
      submitting: false,
      logisticsForm: {
        time: null,
        content: ''
      },
      logisticsRules: {
        time: [
          { required: true, message: '请选择物流时间', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入物流内容', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    orderShow: function (value) {
      if (value) {
        this.dataInit(this.orderData.id)
      }
    }
  },
  methods: {
    showLogisticsModal () {
      this.logisticsModalVisible = true
      this.logisticsForm = {
        time: this.$moment(),
        content: ''
      }
    },
    handleLogisticsCancel () {
      this.logisticsModalVisible = false
      this.$refs.logisticsForm.resetFields()
    },
    submitLogistics () {
      this.$refs.logisticsForm.validate(valid => {
        if (!valid) return

        this.submitting = true

        const newLogistics = {
          time: this.logisticsForm.time.format('YYYY-MM-DD HH:mm:ss'),
          content: this.logisticsForm.content
        }

        const updatedLogisticsList = [newLogistics, ...this.logisticsList]

        this.$put('/cos/order-info/updateLogistics', {
          id: this.orderData.id,
          logisticsContent: JSON.stringify(updatedLogisticsList)
        }).then(() => {
          this.$message.success('物流信息更新成功')
          this.logisticsList = updatedLogisticsList
          this.handleLogisticsCancel()

          this.$emit('refresh')
        }).catch(() => {
          this.$message.error('物流信息更新失败')
        }).finally(() => {
          this.submitting = false
        })
      })
    },
    dataInit (orderId) {
      this.$get(`/cos/order-info/${orderId}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.merchantInfo = r.data.merchant
        this.orderItemInfo = r.data.orderItem
        this.addressInfo = r.data.address
        this.staffInfo = r.data.staff
        this.evaluateInfo = r.data.evaluate
        this.imagesInit(this.merchantInfo.images)
        if (this.orderData.logisticsContent) {
          this.logisticsList = JSON.parse(this.orderData.logisticsContent)
        } else {
          this.logisticsList = []
        }
      })
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
