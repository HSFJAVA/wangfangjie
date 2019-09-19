<template>
  <div class="expotrEnt">
    <div>
      <el-button type="primary" size="small" @click="save()" :disabled="disabledBtn" plain>保存</el-button>
      <!-- <el-button type="primary" plain>新增</el-button>
      <el-button type="primary" plain>另存为</el-button>
      <el-button type="primary" plain>打印</el-button>-->
      <el-button type="primary" size="small" @click="egis()" :disabled="disabledBtn" plain>审核通过</el-button>
      <el-button type="primary" size="small" @click="egisFail()" :disabled="disabledBtn2" plain>驳回</el-button>
      <el-alert
        style="float:right; width:300px"
        v-if="showTip"
        :title="titMessage"
        type="success"
        show-icon
      ></el-alert>
    </div>
    <br>
    <div>
      <el-form label-width="80px" size="mini" :model="detail">
        <!-- 第一行  -->
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="唯一编号">
              <el-input v-model="detail.ywNo" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="业务编号">
              <el-input v-model="detail.customNo" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
                    <el-col :span="6">
            <el-form-item label="货主ID">
              <el-input v-model="detailHead.storeid" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="单证类型">
              <el-input v-model="detail.type" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="6">
            <el-form-item label="报文类型">
              <el-input v-model="detailHead.messagetype"></el-input>
            </el-form-item>
          </el-col>
         <el-col :span="6">
            <el-form-item label="报文类型">
              <el-input v-model="detailHead.doctype"></el-input>
            </el-form-item>
          </el-col>-->
        </el-row>
        <!-- 第一行  -->
        <el-row :gutter="20">
          <!-- <el-col :span="6">
            <el-form-item label="仓库标识">
              <el-input v-model="detailHead.warehouseid" disabled="disabled"></el-input>
            </el-form-item>
          </el-col> -->
          <el-col :span="6">
            <el-form-item label="更新标准">
              <el-input v-model="detailHead.updateflag" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <!-- <el-form-item label="进出口岸">
              <el-input v-model="detailHead.spotservice03"></el-input>
            </el-form-item> -->
            <el-form-item label="进出口岸">
              <el-select v-model="detailHead.spotservice03" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsCust1"
                   :key="item.customsName"
                    :label="item.customsName + '  ['+item.customsCode+']'"
                    :value="item.customsCode">
                  </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="收货人">
              <el-input v-model="detailHead.consigneeid"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="运输方式">
              <!-- <el-input v-model="detailHead.transportation"></el-input> -->
              <el-select
                v-model="detailHead.transportation"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCodeTrans"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsName"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第三行 -->
        <el-row :gutter="20">
                    <el-col :span="6">
            <el-form-item label="运单号">
              <el-input v-model="detailHead.billoflading"></el-input>
            </el-form-item>
          </el-col>
            <el-col :span="6">
            <!-- <el-form-item label="监管方式">
              <el-input v-model="detailHead.spotservice04"></el-input>
            </el-form-item> -->
             <el-form-item label="监管方式">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select v-model="detailHead.spotservice04" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsTrade"
                    :key="item.customsName"
                    :label="item.customsName + '  ['+item.customsCode+']'"
                    :value="item.customsCode">
                  </el-option>
              </el-select>
            </el-form-item>
          </el-col>
           <el-col :span="6">
            <!-- <el-form-item label="征免方式">
              <el-input v-model="detailHead.spotservice02"></el-input>
            </el-form-item> -->
             <el-form-item label="征免方式">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select v-model="detailHead.spotservice02" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsDuty"
                    :key="item.customsName" 
                    :label="item.customsName + '  ['+item.customsCode+']'"
                    :value="item.customsCode"> 
                  </el-option>
              </el-select>
            </el-form-item>
          </el-col>
               <el-col :span="6">
            <!-- <el-form-item label="征免方式">
              <el-input v-model="detailHead.spotservice02"></el-input>
            </el-form-item> -->
             <el-form-item label="成交方式">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select v-model="detailHead.spotservice01" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsCodeDeli"
                    :key="item.customsName" 
                    :label="item.customsName + '  ['+item.customsCode+']'"
                    :value="item.customsCode"> 
                  </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="6">
            <el-form-item label="收货人">
              <el-input v-model="detailHead.shippername"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="成交方式">
              <el-input v-model="detailHead.deliveryterms"></el-input>
                <el-select v-model="detailHead.deliveryterms" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsCodeDeli"
                    :key="item.customsName"
                    :label="item.customsName + '  ['+item.customsCode+']'"
                    :value="item.customsCode">
                  </el-option>
                </el-select>
            </el-form-item>
          </el-col>-->
        </el-row>
        <!-- 第四行 -->
        <el-row :gutter="20">
          <!-- <el-col :span="6">
            <el-form-item label="件数">
              <el-input v-model="detailHead.containerqty"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="包装">
               <el-select v-model="detailHead.containertype" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsPacktype"
                   :key="item.customsName"
                    :label="item.customsName + '  ['+item.customsCode+']'"
                    :value="item.customsCode">
                  </el-option>
              </el-select>
            </el-form-item>
          </el-col>-->
                          <el-col :span="6">
            <el-form-item label="运抵国">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select
                v-model="detailHead.placeofdischarge"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCountry"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']' + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
                    <el-col :span="6">
            <el-form-item label="指运港">
              <!-- <el-select v-model="detailHead.placeofdelivery" :loading="loading2"   filterable remote reserve-keyword :remote-method="remoteMethod" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsCust"
                   :key="item.id"
                    :label="item.customsName + '  ['+item.customsCode+']'"
                    :value="item.customsCode">
                  </el-option>
              </el-select>-->
              <el-select
                v-model="detailHead.placeofdelivery"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCountry"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']' +  '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="出库单号">
              <el-input v-model="detailOut.docno"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="序列号">
              <el-input v-model="detailOut.serialno"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="扫描时间">
              <el-input v-model="detailOut.scantime"></el-input>
            </el-form-item>
          </el-col>
        </el-row>-->
      </el-form>
    </div>

    <div>
      <!-- <el-button type="primary" round size="mini" @click="saveDetailTable()" >保存</el-button> -->
      <el-button type="primary" round size="mini" @click="addnewFormlist()">新增</el-button>
      <!-- <el-button type="primary" round size="mini">删除</el-button>
      <el-button type="primary" round size="mini">复制</el-button>
      <el-button type="primary" round size="mini">上移</el-button>
      <el-button type="primary" round size="mini">下移</el-button>-->
    </div>

    <div>
      <el-table
        :data="tableData"
        stripe
        style="width: 100%"
        height="300px"
        border
        @row-click="getDetails"
        :highlight-current-row="true"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column type="index" width="50"></el-table-column>
        <el-table-column label="操作" width="230">
          <template slot-scope="scope">
            <!-- <el-button size="mini"  @click="handleDelete(scope.$index, scope.row)">编辑</el-button> -->
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            <el-button size="mini" type @click="openBanchDig(scope.$index, scope.row)">batch（添加）</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="customerlineno" label="发票号"></el-table-column>
        <!-- <el-table-column prop="storeid" label="货主id"></el-table-column> -->
        <!-- <el-table-column prop="lottable01" label="batch号"></el-table-column>
        <el-table-column prop="qtyordered" label="batch数量"></el-table-column>-->
        <el-table-column prop="doclineno" label="行号"></el-table-column>
        <el-table-column prop="sku" label="料号"></el-table-column>
        <el-table-column prop="openqty" label="数量"></el-table-column>
        <el-table-column prop="price" label="单价"></el-table-column>
        <el-table-column prop="totalprice" label="总价"></el-table-column>
        <el-table-column prop="currency" label="币制"></el-table-column>
        <el-table-column prop="countryoforigin" label="原产国"></el-table-column>
        <!-- <el-table-column prop="grossweight" label="毛重"></el-table-column>
        <el-table-column prop="netweight" label="净重"></el-table-column>-->
        <!-- <el-table-column prop="cubic" label="体积"></el-table-column> -->
      </el-table>
      <div class="totleNum">
         <span>总数量：</span>
         <span class="fontclolr">{{totqty}}</span>
        <span>总金额：</span>
        <span class="fontclolr">{{totprice.toFixed(2)}}</span>
      </div>
    </div>
    <div>
      <el-form label-width="80px" size="mini">
        <!-- 第一行  -->
        <el-row :gutter="20">
          <el-col :span="4">
            <el-form-item label="发票号">
              <el-input v-model="row.customerlineno"></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="4">
            <el-form-item label="batch号">
              <el-input v-model="row.lottable01"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="batch数量">
              <el-input v-model="row.qtyordered"></el-input>
            </el-form-item>
          </el-col>-->
          <el-col :span="4">
            <!-- <el-form-item label="货主ID">
              <el-input v-model="row.storeid"></el-input>
            </el-form-item>-->
          </el-col>
        </el-row>
        <!-- 第一行  -->
        <el-row :gutter="20">
          <el-col :span="4">
            <el-form-item label="行号">
              <el-input v-model="row.doclineno"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="料号">
              <el-input v-model="row.sku"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="数量">
              <el-input v-model="row.openqty"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="单价">
              <el-input v-model="row.price"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="总价">
              <el-input v-model="row.totalprice"></el-input>
            </el-form-item>
          </el-col>
        </el-row> 
        <!-- 第三行 -->
        <el-row :gutter="20">
          <el-col :span="4">
            <el-form-item label="币制">
              <!-- <el-input v-model="row.currency"></el-input> -->
              <el-select v-model="row.currency" filterable placeholder="请选择" style="width:100%">
                <el-option
                  v-for="item in optionsCodeCurr"
                  :key="item.ncode"
                  :label="item.customsName + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="原产国">
              <!-- <el-input v-model="row.countryoforigin"></el-input> -->
              <el-select
                v-model="row.countryoforigin"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCountry"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']' + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="4">
            <el-form-item label="体积">
              <el-input v-model="row.cubic"></el-input>
            </el-form-item>
          </el-col>-->
        </el-row>
      </el-form>
    </div>
    <el-dialog title="batch相关操作" :visible.sync="dialogTableVisible" width="30%">
      <el-button type="primary" round size="mini" @click="addBatch()">添加</el-button>
      <el-button type="primary" round size="mini" @click="saveBatch()">保存</el-button>
      <!-- <el-button type="danger" round size="mini" @click="deltetGriddata()">删除</el-button> -->
      <el-table
        :data="gridData"
        tooltip-effect="dark"
        @selection-change="SelectionChange"
        border
        stripe
        size="mini"
        show-summary
        :summary-method="getSummaries"
        :highlight-current-row="true"
        height="300px"
        style=" width:100%"
      >
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <!-- <el-button size="mini"  @click="handleDelete(scope.$index, scope.row)">编辑</el-button> -->
            <el-button size="mini" type="danger" @click="handleDelete2(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
        <!-- <el-table-column type="selection"></el-table-column> -->

        <el-table-column property="lottable01" label="batch号">
          <template slot-scope="scope">
            <el-form>
              <el-form-item
                :prop="'gridData.' + scope.$index + '.lottable01'"
                style="margin-bottom:0px !important"
              >
                <el-input size="mini" v-model="scope.row.lottable01" :maxlength="50"></el-input>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column property="qtyordered" label="batch数量">
          <template slot-scope="scope">
            <el-form>
              <el-form-item
                :prop="'gridData.' + scope.$index + '.qtyordered'"
                style="margin-bottom:0px !important"
              >
                <el-input size="mini" v-model="scope.row.qtyordered" :maxlength="50"></el-input>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column type="index"></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "exportenter",
  data() {
    return {
      gridData: [],
      detail: {},
      detailHead: {},
      tableData: [],
      detailOut: {},
      optionsCodeDeli: [],
      optionsCountry: [],
      optionsCodeTrans: [],
      optionsCodeCurr: [],
      optionsCust: [],
      multipleSelection: [],
      dialogTableVisible: false,
      optionsPacktype: [],
      optionsTrade:[],
      totqty: 0,
      session:"",
      showindex: 0,
      optionsDuty:[],
      verifyStart: "待修改",
      totprice: 0,
      totnetweight: 0,
      totgrossweight: 0,
      totvol: 0,
      disabledBtn2: false,
      tempArr: [],
      optionsCust1:[],
      dnNumber: [],
      loading: false,
      loading2: false,
      disabledBtn: false,
      showTip: false,
      titMessage: "",
      row: {},
      o: {}
    };
  },
  mounted: function() {
    console.log(this.$route.query);
    this.detail = this.$route.query;
    this.detailHead.soreference1 = this.detail.customNo;
    this.detailHead.wlYwno = this.detail.ywNo;
    if (this.detail.status !== "未录入") {
      this.searchDetail();
    }
    this.searchparm();
    this.getsession();
  },
  methods: {
    searchDetail() {
      this.loading = false;
      const tthis = this;
      this.$http
        .get(
          `/apis/kwe/index/getDetailByNo?wlywno=${this.detail.ywNo}&type=${
            this.detail.type
          }`
        )
        .then(function(resp) {
          console.log("获取回来的数据");
          console.log(resp);
          for (let i = 0; i < resp.data.head.length; i++) {
            for (let k = 0; k < resp.data.detail.length; k++) {
              if (
                resp.data.detail[k].messagehead == resp.data.head[i].messagehead
              ) {
                resp.data.detail[k].dUserdefine2 =
                  resp.data.head[i].asnreference1;
              }
            }
          }
          tthis.detailHead = resp.data.head[0];
          tthis.tableData = resp.data.detail;
          tthis.loading = false;
          if (tthis.tableData.length > 0) {
            tthis.tableData.forEach(res => {
              tthis.totqty += res.openqty * 1;
              tthis.totprice += res.totalprice * 1;
              tthis.totnetweight += res.netweight * 1;
              tthis.totgrossweight += res.grossweight * 1;
              tthis.totvol += res.cubic * 1;
                            if(tthis.detailHead.status == 2) {  //已审核
              tthis.disabledBtn = true;
              tthis.showTip = true;
              tthis.disabledBtn2 = false;
              tthis.titMessage = "已审核通过，如需修改请先驳回"
            };
            if(tthis.detailHead.status == 3) { // "已同步" 
              tthis.disabledBtn = true;
              tthis.showTip = true;
              tthis.disabledBtn2 = false;
              tthis.titMessage = "该票单证已同步"
            }
            if(tthis.detailHead.status == 4) {  //"已完成"
              tthis.disabledBtn = true;
              tthis.showTip = true;
              tthis.disabledBtn2 = true;
              tthis.titMessage = "该票单证是已完成状态"
            }
            if(tthis.detailHead.status == 6) {  //"待修改"
              tthis.disabledBtn = false;
              tthis.showTip = true;
              tthis.disabledBtn2 = false;
              tthis.titMessage = "该票单证待修改"
            }
            if(tthis.detailHead.status == 5) { // "已修改"
              tthis.disabledBtn = false;
              tthis.showTip = true;
              tthis.disabledBtn2 = false;
              tthis.titMessage = "该票单证已修改"
            }
            if(tthis.detailHead.status == 1) {  // "待审核"
              tthis.disabledBtn = false;
              tthis.showTip = true;
              tthis.disabledBtn2 = true;
              tthis.titMessage = "该票单证是待审核状态"
            }
            });
          }
        })
        .catch(function(err) {
          console.log(err);
        });
    },
    getDetails(row) {
      this.row = row;
    },
    SelectionChange(val, index) {
      console.log(val);
      console.log(index);
      this.multipleSelection = val;
    },
    handleDelete(index, row) {
      this.tableData.splice(index, 1);
    },
    handleDelete2(index, row) {
      this.gridData.splice(index, 1);
    },
    saveBatch(index, row) {
      console.log(this.gridData);
      this.tableData[this.showindex].remark1 = this.gridData;
      console.log(this.tableData);
      this.$message({
        message: "保存成功",
        type: "success"
      });
      this.dialogTableVisible = false;
    },
    addBatch() {
      this.gridData.push({
        lottable01: "",
        qtyordered: ""
      });
    },
    deltetGriddata() {
      console.log(this.multipleSelection);
      if (this.multipleSelection.length < 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          this.gridData.splice(i, 1);
        }
      }
    },
    openBanchDig(index, row) {
      console.log(row);
      this.showindex = index;
      if (row.remark1) {
        this.gridData = row.remark1;
      } else {
        row.remark1 = [];
        this.gridData = row.remark1;
      }
      this.dialogTableVisible = true;
    },
    uniq(array) {
      var temp = []; //一个新的临时数组
      for (var i = 0; i < array.length; i++) {
        if (temp.indexOf(array[i]) == -1) {
          temp.push(array[i]);
        }
      }
      return temp;
    },
    searchparm() {
      const tthis = this;
      this.$http
        .get(`/apis/kwe/index/selectCode?dictType=9`)
        .then(function(resp) {
          tthis.optionsCodeDeli = resp.data;
        });
      this.$http
        .get(`/apis/kwe/index/selectCode?dictType=1`)
        .then(function(resp) {
          let hash = {};
          resp.data = resp.data.reduce((preVal, curVal) => {
            hash[curVal.customsCode]
              ? ""
              : (hash[curVal.customsCode] = true && preVal.push(curVal));
            return preVal;
          }, []);
          tthis.optionsPacktype = resp.data;
        });
      this.$http
        .get(`/apis/kwe/index/selectCode?dictType=6`)
        .then(function(resp) {
          console.log(resp);
          tthis.optionsCountry = resp.data;
        });
      // this.$http
      // .get(`/apis/kwe/index/selectCode?dictType=4`)
      // .then(function(resp) {
      //   console.log(resp);
      //   tthis.optionsCust  = resp.data;
      // });
      this.$http
        .get(`/apis/kwe/index/selectCode?dictType=8`)
        .then(function(resp) {
          tthis.optionsCodeTrans = resp.data;
        });
      this.$http
        .get(`/apis/kwe/index/selectCode?dictType=17`)
        .then(function(resp) {
          tthis.optionsDuty = resp.data;
        });
      this.$http
        .get(`/apis/kwe/index/selectCode?dictType=5`)
        .then(function(resp) {
          tthis.optionsCust1 = resp.data;
        });
        this.$http
        .get(`/apis/kwe/index/selectCode?dictType=16`)
        .then(function(resp) {
          tthis.optionsTrade = resp.data;
        });
      this.$http
        .get(`/apis/kwe/index/selectCode?dictType=2`)
        .then(function(resp) {
          tthis.optionsCodeCurr = resp.data;
        });
    },
    remoteMethod(query) {
      const tthis = this;
      if (query !== "") {
        this.loading2 = true;
        setTimeout(() => {
          this.loading2 = false;
          this.$http
            .get(`/apis/kwe/index/selectCode?dictType=39`)
            .then(function(resp) {
              tthis.optionsCust = resp.data.filter(item => {
                return (
                  item.customsName.toLowerCase().indexOf(query.toLowerCase()) >
                  -1
                );
              });
            });
        }, 200);
      }
    },
    egis() {
      console.log(this.detail.ywNo);
      const tthis = this;
      this.$http
        .post(`/apis/kwe/index/audit?wlywno=${this.detail.ywNo}&status=1&op_user=${this.session}`)
        .then(resp => {
          console.log(resp);
          if (resp.data == "updateNice") {
            tthis.$message({
              message: "更改状态成功",
              type: "success"
            });
            tthis.searchDetail();
          } else {
            tthis.$message({
              message: "保存失败",
              type: "error"
            });
          }
        });
    },
    egisFail() {
      console.log(this.detail.ywNo);
      if(this.detailHead.status == 2) {
        this.verifyStart = "待审核"
      }
      if(this.detailHead.status == 3) {
        this.verifyStart = "待修改"
      }
      const tthis = this;
      this.$http
        .post(`/apis/kwe/index/audit?wlywno=${this.detail.ywNo}&status=0&op_user=${this.session}`)
        .then(resp => {
          if (resp.data == "updateNice") {
            tthis.$message({
              message: "更改状态成功",
              type: "success"
            });
           tthis.searchDetail();
          } else {
            tthis.$message({
              message: "保存失败",
              type: "error"
            });
          }
        });
    },
    saveDetailTable() {
      const tthis = this;
      this.dnNumber = [];
      this.tempArr = [];
      if (this.tableData.length > 0) {
        this.tableData.forEach(function(res) {
          res.storeid = "C000002";
          res.price = res.totalprice / res.openqty;
          tthis.dnNumber.push(res.customerlineno);
          let array = tthis.o[res["customerlineno"]] || [];
          array.push(res);
          tthis.o[res["customerlineno"]] = array;
        });
        for (let item in tthis.o) {
          if (tthis.o[item].length > 1) {
            let indexO = 0;
            this.$http
              .get("/apis/kwe/serialno/getMessageHead")
              .then(function(res) {
                tthis.o[item].forEach(resp => {
                  resp.messagehead = res.data;
                  tthis.tempArr.push(tthis.o[item][0].messagehead);
                });
              });
          } else {
            this.$http
              .get("/apis/kwe/serialno/getMessageHead")
              .then(function(res) {
                tthis.o[item][0].messagehead = res.data;
                tthis.tempArr.push(tthis.o[item][0].messagehead);
              });
          }
        }
      }
    },
    getsession() {
        if(sessionStorage.getItem("name")) {
          this.session =  sessionStorage.getItem("name");
        } else {
            this.$router.push('/');
        }
    },
    save() {
      this.detailHead.op_user = this.session;
      if (this.detail.status == "未录入") {
        let tthis = this;
        const detailHead = [];
        console.log(this.detailHead);
        detailHead.push(this.detailHead);
        const row = [];
        const allMessage = [];
        for (var i = 0; i < this.tableData.length; i++) {
          this.tableData[i].price =
            ((this.tableData[i].totalprice * 1) / this.tableData[i].openqty) *
            1;
        }
        const data = {
          outputHeadList: detailHead,
          outputDetailList: this.tableData,
          op_user: this.session
        };
        console.log("this data");
        console.log(data);
        this.$http
          .post(`/apis/kwe/index/outputInsert`, data)
          .then(function(resp) {
            console.log(resp);
            if (resp.data == "saveNice") {
              tthis.$message({
                message: "保存成功",
                type: "success"
              });
              tthis.searchDetail();
            } else {
              tthis.$message({
                message: "保存失败",
                type: "error"
              });
            }
          });
      } else {
        console.log("this data");
        console.log(data);
        let tthis = this;
        // this.uniq(this.dnNumber).forEach(resp => {
        //   headDate.push(JSON.parse(JSON.stringify(this.detailHead)));
        // });
        const row = [];
        for (var i = 0; i < this.tableData.length; i++) {
          this.tableData[i].price =
            ((this.tableData[i].totalprice * 1) / this.tableData[i].openqty) *
            1;
          this.tableData[i].messagehead = this.detailHead.messagehead;
        }
        const detailHead = [];
        detailHead.push(this.detailHead);
        const data = {
          outputHeadList: detailHead,
          outputDetailList: this.tableData,
          op_user: this.session
        };
        console.log("增加了数据");
        console.log(data);
        this.$http
          .post(`/apis/kwe/index/outputUpdate`, data)
          .then(function(resp) {
            console.log("success");
            console.log(resp);
            if (resp.data == "updateNice") {
              tthis.$message({
                message: "修改成功",
                type: "success"
              });
              tthis.searchDetail();
            } else {
              tthis.$message({
                message: "修改失败",
                type: "error"
              });
            }
          });
      }
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "合计";
          return;
        }
        if (index === 1) {
          sums[index] = "";
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return prev + curr;
            } else {
              return prev;
            }
          }, 0);
          sums[index] += "数量";
        } else {
          sums[index] = this.gridData.length;
        }
      });

      return sums;
    },
    addnewFormlist() {
      this.tableData.push({
        // customerlineno: "",
        docno: "",
        serialno: "",
        scantime: "",
        customerlineno: "",
        sku: "",
        openqty: "",
        price: "",
        totalprice: "",
        currency: "",
        storeid: "",
        countryoforigin: "",
        rossweight: "",
        netweight: "",
        cubic: ""
      });
    }
  }
};
</script>

<style>
.totleNum {
  margin: 10px;
  text-align: center;
}
.totleNum span {
  margin-right: 20px;
}
.fontclolr {
  color: #f56c6c;
}
</style>


