<template>
  <div class="expotrEnt app-container" v-loading="loading" element-loading-text="拼命加载中" @keyup.enter="keyCodeNext">
    <div>
      <el-button type="primary" size="small" @click="save()" :disabled="disabledBtn" plain>保存</el-button>
      <el-button type="primary" size="mini"  @click="dialogFormVisible = true" plain>数据导入</el-button>
      <!-- <el-button type="primary" size="mini" @click="weightSplitting()"  plain>重量拆分</el-button> -->
      <el-button type="primary" size="small" plain @click="exportExcel()">导出Excel</el-button>
      <el-button type="success" size="small" @click="egis()" :disabled="disabledBtn" plain>审核通过</el-button>
      <el-button type="danger" size="small" @click="egisFail()" :disabled="disabledBtn2" plain>驳回</el-button>
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
      <el-form label-width="120px" size="mini" :model="detailHead" :rules="rules" ref="detailHead">
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
              <el-input v-model="detail.remark1" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="单证类型">
              <el-input v-model="detail.type" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
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
            <el-form-item label="进出口岸" prop="spotservice03">
              <el-select clearable v-model="detailHead.spotservice03" filterable placeholder="请选择" style="width:100%" @change="$set(detailHead, detailHead.spotservice03, $event)">
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
            <el-form-item label="供应商" prop="consigneeid" v-if="consigneeid.showInput == true">
              <el-input v-model="detailHead.consigneeid" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="consigneeaddress1.showInput == true">
            <el-form-item label="境外收发货人" prop="consigneeaddress1" v-if="consigneeaddress1.showInput == true">
              <el-input v-model="detailHead.consigneeaddress1" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="运输方式" prop="transportation">
              <!-- <el-input v-model="detailHead.transportation"></el-input> -->
              <el-select clearable
                v-model="detailHead.transportation"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCodeTrans"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="运单号" prop="billoflading" v-if="billoflading.showInput == true">
              <el-input v-model="detailHead.billoflading"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="soreference2.showInput == true">
            <el-form-item label="合同协议号" prop="soreference2" v-if="soreference2.showInput == true">
              <el-input v-model="detailHead.soreference2"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" :gutter="20" v-if="countryoforigin1.showInput == true">
            <el-form-item label="贸易国别(地区)" prop="countryoforigin">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select clearable
                v-model="detailHead.countryoforigin"
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
             <el-form-item label="监管方式"  prop="spotservice04">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select clearable v-model="detailHead.spotservice04" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsTrade"
                    :key="item.customsName"
                    :label="item.customsName + '  ['+item.customsCode+']'"
                    :value="item.customsCode">
                  </el-option>
              </el-select>
            </el-form-item>
          </el-col>

           <el-col :span="6" v-if="spotservice05.showInput == true">
             <el-form-item label="征免性质"  prop="spotservice05">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select clearable v-model="detailHead.spotservice05" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsCutmode"
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
             <el-form-item label="征免方式" prop="spotservice02">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select clearable v-model="detailHead.spotservice02" filterable placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in optionsDuty"
                    :key="item.customsName" 
                    :label="item.customsName + '  ['+item.customsCode+']'"
                    :value="item.customsCode"> 
                  </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="consigneeaddress2.showInput == true">
            <el-form-item label="成交方式" style="width:100%"  prop="consigneeaddress2">
              <!-- <el-input v-model="detailHead.deliveryterms"></el-input> -->
              <el-select clearable
                v-model="detailHead.consigneeaddress2"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCodeDeli"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        <!-- 第四行 -->
           <el-col :span="6" :gutter="20">
            <el-form-item label="运抵国" prop="placeofdischarge">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select clearable
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
            <el-form-item label="指运港" prop="placeofdelivery"> 
              <el-select clearable
                v-model="detailHead.placeofdelivery"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsPort"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']' +  '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="containertype.showInput == true">
            <el-form-item label="包装种类" prop="containertype"> 
              <el-select clearable
                v-model="detailHead.containertype"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsPacktype"
                  :key="item.customsName"
                  :label="item.name + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <div>
      <el-button type="primary" round size="mini" @click="addnewFormlist()">新增</el-button>
      <el-button round size="mini" @click="copylist()">复制</el-button>
      <el-button round size="mini" @click="modifyTable()">修改</el-button>
      <el-button round size="mini" @click.native="dignetweight1out( 'totalprice' ,'volumn')">拆分（分金额）</el-button>  
         <el-dropdown>
          <el-button  round size="mini">
            拆分（单价）<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="priceSave(0)">单价拆分（取整）</el-dropdown-item>
            <el-dropdown-item @click.native="priceSave(1)">单价拆分（保留1位小数）</el-dropdown-item>
            <el-dropdown-item @click.native="priceSave(2)">单价拆分（保留2位小数）</el-dropdown-item>
            <el-dropdown-item @click.native="priceSave(3)">单价拆分（保留3位小数）</el-dropdown-item> 
            <el-dropdown-item @click.native="priceSave(4)">单价拆分（保留4位小数）</el-dropdown-item>
            <el-dropdown-item @click.native="priceSave(5)">单价拆分（保留5位小数）</el-dropdown-item>
            <el-dropdown-item @click.native="priceSave(6)">单价拆分（保留6位小数）</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-button round size="mini" @click="synchroTo()">同步商品库</el-button>
        <el-button round size="mini" @click="calculatePrice()">计算总价</el-button>
    </div>

    <div>
      <el-table
        :data="tableData"
        stripe
        style="width: 100%"
        height="400px"
        @selection-change="handleSelectionChange"
        :summary-method="getSummariesTable"
        :row-class-name="tableRowClassName"
        show-summary
        border
        @row-click="getDetails"
        :highlight-current-row="true"
        ref="TableToggle"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column type="index" width="50" fixed></el-table-column>
        <el-table-column label="操作" width="220">
          <template slot-scope="scope">
            <!-- <el-button size="mini"  @click="handleDelete(scope.$index, scope.row)">编辑</el-button> -->
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            <el-button  v-if="btnAdd == true" size="mini" type @click="openBanchDig(scope.$index, scope.row)">添加</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="customerlineno" label="发票号" width="180" :render-header="renderHeader"></el-table-column>
        <!-- <el-table-column prop="sku" label="料号"></el-table-column> -->
                <el-table-column prop="sku" label="料号" width="180" :render-header="renderHeader">
          <template slot-scope="scope">
            <div :style="{color: scope.row.remark.sku == 0 ? '#F56C6C' : 'green'}">
              {{scope.row.sku}}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="openqty" label="数量" width="160" :render-header="renderHeader"></el-table-column>
        <el-table-column prop="declarationunit" label="单位" width="160" v-if="declarationunit.showInput == true" :render-header="renderHeader"></el-table-column>
        <el-table-column prop="price" label="单价" :render-header="renderHeader"></el-table-column>
        <el-table-column prop="totalprice" label="总价" :render-header="renderHeader"></el-table-column>
        <el-table-column prop="currency" label="币制" :render-header="renderHeader"></el-table-column>
        <el-table-column prop="countryoforigin" v-if="countryoforigin.showInput == true"
         label="原产国" :render-header="renderHeader"
         ></el-table-column>
        <!-- <el-table-column prop="doclineno" label="行号"></el-table-column> -->
                <!-- 显示隐藏部分 -->
        <el-table-column
          width="150"
          v-if="doclineno.showInput == true && doclineno.showFlag == false"
          prop="doclineno"
          :label="doclineno.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="150"
          v-if="lottable01.showInput == true && lottable01.showFlag == false"
          prop="lottable01"
          :label="lottable01.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="150"
          v-if="lottable02.showInput == true && lottable02.showFlag == false"
          prop="lottable02"
          :label="lottable02.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="150"
          v-if="lottable03.showInput == true && lottable03.showFlag == false"
          prop="lottable03"
          :label="lottable03.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="150"
          v-if="lottable04.showInput == true && lottable04.showFlag == false"
          prop="lottable04"
          :label="lottable04.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="150"
          v-if="lottable05.showInput == true && lottable05.showFlag == false"
          prop="lottable05"
          :label="lottable05.value"
          :render-header="renderHeader"
        ></el-table-column>
         <el-table-column
          width="150"
          v-if="volumnuom.showInput == true && volumnuom.showFlag == false"
          prop="volumnuom"
          :label="volumnuom.value"
          :render-header="renderHeader"
        ></el-table-column> 
                 <el-table-column
          width="150"
          v-if="containernum.showInput == true && containernum.showFlag == false"
          prop="containernum"
          :label="containernum.value"
          :render-header="renderHeader"
        ></el-table-column>
      </el-table>
    </div>
    <div>
      <el-form label-width="80px" size="mini" v-if="showRow">
        <!-- 第一行  -->
         <h3>关务信息 <span class="messageIndex">(第 <span class="messageIndexIn">{{row.index+1}}</span> 行的信息)</span></h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="发票号">
              <el-input v-model="row.customerlineno"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="料号">
              <el-input v-model="row.sku"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="数量">
              <el-input v-model="row.openqty"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6"  v-if="declarationunit.showInput == true ">
            <el-form-item  label="单位">
              <el-input v-model.trim="row.declarationunit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="单价">
              <el-input v-model="row.price"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="总价">
              <el-input v-model="row.totalprice"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="币制">
              <!-- <el-input v-model="row.currency"></el-input> -->
              <el-select clearable v-model="row.currency" filterable placeholder="请选择" style="width:100%">
                <el-option
                  v-for="item in optionsCodeCurr"
                  :key="item.ncode"
                  :label="item.customsName + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" >
            <el-form-item label="原产国" v-if="countryoforigin.showInput == true">
              <!-- <el-input v-model="row.countryoforigin"></el-input> -->
              <el-select clearable
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
        </el-row>


        <!-- 第三行 -->
         <h3>仓储信息</h3>
        <el-row :gutter="20">
        <!-- 显示隐藏部分 -->
                  <el-col :span="6">
            <el-form-item
              :label="doclineno.value"
               v-if="doclineno.showInput == true && doclineno.showFlag == false"
              label-width="100px"
            >
              <el-input v-model="row.doclineno"></el-input>
            </el-form-item>
            </el-col>
          <el-col :span="6">
            <el-form-item
              :label="lottable01.value"
              v-if="lottable01.showInput == true  && lottable01.showFlag == false"
              label-width="100px"
            >
              <el-input v-model="row.lottable01"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              :label="lottable02.value"
              v-if="lottable02.showInput == true  && lottable02.showFlag == false"
              label-width="100px"
            >
              <el-input v-model="row.lottable02"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              :label="lottable03.value"
              v-if="lottable03.showInput == true && lottable03.showFlag == false"
              label-width="100px"
            >
              <el-input v-model="row.lottable03"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              :label="lottable04.value"
              v-if="lottable04.showInput == true && lottable04.showFlag == false"
              label-width="100px"
            >
              <el-input v-model="row.lottable04"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              :label="lottable05.value"
              v-if="lottable05.showInput == true  && lottable05.showFlag == false"
              label-width="100px"
            >
              <el-input v-model="row.lottable05"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              :label="volumnuom.value"
              v-if="volumnuom.showInput == true && volumnuom.showFlag == false"
              label-width="100px"
            >
              <el-input v-model="row.volumnuom"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
          <el-form-item
              :label="containernum.value"
              v-if="containernum.showInput == true && containernum.showFlag == false"
              label-width="100px"
            >
              <el-input v-model="row.containernum"></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="6">
            <el-form-item label="体积">
              <el-input v-model="row.cubic"></el-input>
            </el-form-item>
          </el-col>-->
        </el-row>
      </el-form>
    </div>
    <el-dialog title="batch相关操作" :visible.sync="dialogTableVisible" width="40%">
      <el-button type="primary" round size="mini" @click="addBatch()">添加</el-button>
      <el-button type="primary" round size="mini" @click="saveBatch()">保存</el-button>
      <el-button type="primary" style="float:right" round size="mini" @click="digMoney()">拆分</el-button>
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
               <!-- 动态改变部分 -->
          <el-table-column
            property="lottable01"
            v-if="lottable01.showFlag == true && lottable01.showFlag == true"
            :label="lottable01.value"
          >
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
          <el-table-column
            property="lottable02"
            v-if="lottable02.showFlag == true && lottable02.showFlag == true"
            :label="lottable02.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item
                  :prop="'gridData.' + scope.$index + '.lottable02'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model="scope.row.lottable02" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            property="lottable03"
            v-if="lottable03.showFlag == true  && lottable03.showFlag == true"
            :label="lottable03.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item
                  :prop="'gridData.' + scope.$index + '.lottable03'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model="scope.row.lottable03" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            property="lottable04"
            v-if="lottable04.showFlag == true && lottable04.showFlag == true"
            :label="lottable04.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item
                  :prop="'gridData.' + scope.$index + '.lottable04'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model="scope.row.lottable04" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            property="lottable05"
            v-if="lottable05.showFlag == true && lottable05.showFlag == true"
            :label="lottable05.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item
                  :prop="'gridData.' + scope.$index + '.lottable05'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model="scope.row.lottable05" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            property="qtyordered"
            v-if="qtyordered.showFlag == true && qtyordered.showFlag == true"
            :label="qtyordered.value"
          >
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
          <el-table-column
            property="volumn"
            v-if="volumn.showFlag == true && volumn.showFlag == true"
            :label="volumn.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item
                  :prop="'gridData.' + scope.$index + '.volumn'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model="scope.row.volumn" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
        <el-table-column type="index"></el-table-column>
      </el-table>
    </el-dialog>


      <!-- 上传的model -->
    <el-dialog title="上传列表" :visible.sync="dialogFormVisible" width="35%">
  <el-form :model="form">
    <el-form-item  style="text-align:center">
        <el-upload
        class="upload-demo"
        drag
        :file-list="form.file"
        :on-change="successUpload"
        :on-remove="Remove"
        :before-upload="beforeAvatarUpload"
        action="https://jsonplaceholder.typicode.com/posts/"
        multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      </el-upload>
      <!-- <input type="file" @change="tirggerFile($event)"> -->
    </el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button @click="dialogFormVisible = false">取 消</el-button>
    <el-button type="primary" @click="dataBaseImport($event, file)">确 定</el-button>
  </div>
</el-dialog>

        <!-- 这是编辑的dialogvislable -->
    <el-dialog title="修改內容" :visible.sync="dialogFormVisibleEdit" center width="30%">
      <el-form :model="formYwEdit" size="mini">
        <el-form-item :label="formYwEdit.label" :label-width="formLabelWidth">
          <el-input v-model="formYwEdit.content" auto-complete="off"></el-input>
        </el-form-item> 
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisibleEdit = false" size="mini">取 消</el-button>
        <el-button type="primary" @click="eEditYwSelect()"  size="mini">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
/* eslint-disable */
import { custoutUNsame } from "../../utils/outCustRequst";
import { custInUNsame , guangLaiArr1, guanglaiSo1 } from "../../utils/importCustRequst";
import { getsession } from "../../utils/pramAll";
import '../../utils/publicJs';
import merge from 'webpack-merge'
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
      optionsPort:[],
      dialogTableVisible: false,
      optionsPacktype: [],
      dialogFormVisible: false,
      dialogFormVisibleEdit: false,
      optionsTrade:[],
      totqty: 0,
      showRow:false,
      session:"",
      optionsCutmode: [],
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
      headDate:{},
      consigneeid:{},
      multipleSelection1:[],
      volumn:{},
      formYwEdit: {
        label: "",
        content: ""
      },
      consigneeaddress1: {},
      soreference2: {},
      countryoforigin: {},
      spotservice05: {},
      consigneeaddress2: {},
      containertype: {},
      allweigthTot : 0,
      allnegihtTot : 0,
      templateJson: {},
      lottable01: {},
      lottable02: {},
      lottable03: {},
      lottable04: {},
      lottable05: {},
      detailHeadRex:{},
      lottable06: {},
      billoflading: {},
      countryoforigin1:{},
      declarationunit:{},
      lottable07: {},
      lottable08: {},
      lottable09: {},
      lottable10: {},
      lottable11: {},
      lottable12: {},
      addJsonObj:{},
      doclineno:{},
      qtyordered:{},
      volumnuom:{},
      containernum:{},
      file:{},
      rules: {
          placeofdischarge: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          transportation: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          consigneeid: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          billoflading: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          placeofdelivery: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          spotservice01: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          spotservice02: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          spotservice03: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          spotservice04: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          templateName: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          consigneeaddress1: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          soreference2: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          countryoforigin: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          spotservice05: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          consigneeaddress2: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          containertype: [{ required: true, message: '值不能为空', trigger: 'blur' }],
          countryoforigin1:[{ required: true, message: '值不能为空', trigger: 'blur' }],
        },
      btnAdd:"",
      form: {
          name: '',
          file: [],
        }, 
        formLabelWidth: '120px',
      o: {}
    };
  },
    created (){
    this.detail = this.$route.query;
  },
  mounted: function() {
    this.detail = this.$route.query;
    this.detailHead.soreference1 = this.detail.customNo;
    this.detailHead.wlYwno = this.detail.ywNo;
    this.detailHead.storeid = this.detail.storeid;
    if (this.detail.status !== "未录入") {
      this.searchDetail();
    }else {
      this.getDiffentCust(this.detail);
    }
    this.searchparm();
   this.session = getsession(this.$router);
  },
  methods: {
      searchDetail() {
      this.loading = true;
      const tthis = this;
      this.$http
        .get(
          `${process.env.VUE_APP_BASE_API}kwe/index/getDetailByNo?wlywno=${this.detail.ywNo}&type=${
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
          tthis.getDiffentCust(tthis.detail);
          tthis.loading = false;
          if (tthis.tableData.length > 0) {
            tthis.tableData.forEach(res => {
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
              tthis.disabledBtn2 = false;
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
    modelHead(head) {
      this.detail.status = this.$route.query.status;
        if(this.detail.status == "未录入" || this.detail.status == "已识别待补充") {
          for(let obj in head) {
            if(head[obj] instanceof Object == true && head[obj].value){
              this.detailHead[obj] = head[obj].value;
            }
        }
        } else {
          console.log("这是我们的head");
        }
    },
    getDiffentCust(detail) {
      const tthis = this;
      console.log(detail.remark2);
    //  if(!detail.remark2) {
    //    detail.remark2 = "bbd5d4df-e26b-4bc1-bd31-b321a93c84b1";
    //    alert("创建业务未选择模板");
    //  }
    this.$http
      .post(`${process.env.VUE_APP_BASE_API}kwe/index/getTemplateByModelId`,{modelId: detail.remark2})
      .then(function(resp) {
        tthis.templateJson = JSON.parse(resp.data.template.text);
        tthis.headDate = tthis.templateJson.detailHead;
        tthis.modelHead(tthis.headDate);
    // for(let k in tthis.templateJson.detailHead) {
    //     // tthis.detailHead[k]  = tthis.templateJson.detailHead[k].value;
    //     tthis.detailHeadRex[k] = {
    //             disabled: tthis.templateJson.detailHead[k].disabled,
    //             required: tthis.templateJson.detailHead[k].required,
    //             showInput: tthis.templateJson.detailHead[k].showInput
    //     }
    //   }
        tthis.rules.placeofdischarge[0].required = tthis.templateJson.detailHead.placeofdischarge.required;
        tthis.rules.transportation[0].required = tthis.templateJson.detailHead.transportation.required;
        tthis.rules.consigneeid[0].required = tthis.templateJson.detailHead.consigneeid.required;
        tthis.rules.billoflading[0].required = tthis.templateJson.detailHead.billoflading.required;
        tthis.rules.spotservice01[0].required = tthis.templateJson.detailHead.spotservice01.required;
        tthis.rules.spotservice02[0].required = tthis.templateJson.detailHead.spotservice02.required;
        tthis.rules.spotservice03[0].required = tthis.templateJson.detailHead.spotservice03.required;
        tthis.rules.spotservice04[0].required = tthis.templateJson.detailHead.spotservice04.required;

        tthis.rules.consigneeaddress1[0].required = tthis.templateJson.detailHead.consigneeaddress1.required;
        tthis.rules.soreference2[0].required = tthis.templateJson.detailHead.soreference2.required;
        tthis.rules.countryoforigin[0].required = tthis.templateJson.detailHead.countryoforigin.required;
        tthis.rules.spotservice05[0].required = tthis.templateJson.detailHead.spotservice05.required;
        tthis.rules.consigneeaddress2[0].required = tthis.templateJson.detailHead.consigneeaddress2.required;
        tthis.rules.containertype[0].required = tthis.templateJson.detailHead.containertype.required;
        tthis.rules.countryoforigin[0].required = tthis.templateJson.detailHead.countryoforigin.required;
      tthis.btnAdd = tthis.templateJson.detailDeploy.btnAdd
      tthis.consigneeaddress1 = {
          value: tthis.templateJson.detailHead.consigneeaddress1.value,
          showFlag: tthis.templateJson.detailHead.consigneeaddress1.showFlag,
          showInput: tthis.templateJson.detailHead.consigneeaddress1.showInput
        }
      tthis.soreference2 = {
          value: tthis.templateJson.detailHead.soreference2.value,
          showFlag: tthis.templateJson.detailHead.soreference2.showFlag,
          showInput: tthis.templateJson.detailHead.soreference2.showInput
        }
       tthis.countryoforigin = {
          value: tthis.templateJson.detailHead.countryoforigin.value,
          showFlag: tthis.templateJson.detailHead.countryoforigin.showFlag,
          showInput: tthis.templateJson.detailHead.countryoforigin.showInput
        }
        tthis.countryoforigin1 = {
          value: tthis.templateJson.detailHead.countryoforigin.value,
          showFlag: tthis.templateJson.detailHead.countryoforigin.showFlag,
          showInput: tthis.templateJson.detailHead.countryoforigin.showInput
        }
        tthis.spotservice05 = {
          value: tthis.templateJson.detailHead.spotservice05.value,
          showFlag: tthis.templateJson.detailHead.spotservice05.showFlag,
          showInput: tthis.templateJson.detailHead.spotservice05.showInput
        }
        tthis.consigneeaddress2 = {
          value: tthis.templateJson.detailHead.consigneeaddress2.value,
          showFlag: tthis.templateJson.detailHead.consigneeaddress2.showFlag,
          showInput: tthis.templateJson.detailHead.consigneeaddress2.showInput
        }
        tthis.containertype = {
          value: tthis.templateJson.detailHead.containertype.value,
          showFlag: tthis.templateJson.detailHead.containertype.showFlag,
          showInput: tthis.templateJson.detailHead.containertype.showInput
        }
       tthis.consigneeid = {
          value: tthis.templateJson.detailHead.consigneeid.value,
          showFlag: tthis.templateJson.detailHead.consigneeid.showFlag,
          showInput: tthis.templateJson.detailHead.consigneeid.showInput
        }
       tthis.billoflading = {
          value: tthis.templateJson.detailHead.billoflading.value,
          showFlag: tthis.templateJson.detailHead.billoflading.showFlag,
          showInput: tthis.templateJson.detailHead.billoflading.showInput
        }
        tthis.lottable01 = {
          value: tthis.templateJson.detailDeploy.lottable01.value,
          showFlag: tthis.templateJson.detailDeploy.lottable01.showFlag,
          showInput: tthis.templateJson.detailDeploy.lottable01.showInput
        };
        tthis.lottable02 = {
          value: tthis.templateJson.detailDeploy.lottable02.value,
          showFlag: tthis.templateJson.detailDeploy.lottable02.showFlag,
          showInput: tthis.templateJson.detailDeploy.lottable02.showInput
        };
        tthis.lottable03 = {
          value: tthis.templateJson.detailDeploy.lottable03.value,
          showFlag: tthis.templateJson.detailDeploy.lottable03.showFlag,
          showInput: tthis.templateJson.detailDeploy.lottable03.showInput
        };
        tthis.lottable04 = {
          value: tthis.templateJson.detailDeploy.lottable04.value,
          showFlag: tthis.templateJson.detailDeploy.lottable04.showFlag,
          showInput: tthis.templateJson.detailDeploy.lottable04.showInput
        };
        tthis.lottable05 = {
          value: tthis.templateJson.detailDeploy.lottable05.value,
          showFlag: tthis.templateJson.detailDeploy.lottable05.showFlag,
          showInput: tthis.templateJson.detailDeploy.lottable05.showInput
        };
        tthis.qtyordered = {
          value: tthis.templateJson.detailDeploy.qtyordered.value,
          showFlag: tthis.templateJson.detailDeploy.qtyordered.showFlag,
          showInput: tthis.templateJson.detailDeploy.qtyordered.showInput
        };
        tthis.doclineno = {
          value: tthis.templateJson.detailDeploy.doclineno.value,
          showFlag: tthis.templateJson.detailDeploy.doclineno.showFlag,
          showInput: tthis.templateJson.detailDeploy.doclineno.showInput
        };
        tthis.volumn = {
          value: tthis.templateJson.detailDeploy.volumn.value,
          showFlag: tthis.templateJson.detailDeploy.volumn.showFlag,
          showInput: tthis.templateJson.detailDeploy.volumn.showInput
        };
        tthis.volumnuom = {
          value: tthis.templateJson.detailDeploy.volumnuom.value,
          showFlag: tthis.templateJson.detailDeploy.volumnuom.showFlag,
          showInput: tthis.templateJson.detailDeploy.volumnuom.showInput
        };
        tthis.containernum = {
          value: tthis.templateJson.detailDeploy.containernum.value,
          showFlag: tthis.templateJson.detailDeploy.containernum.showFlag,
          showInput: tthis.templateJson.detailDeploy.containernum.showInput
        };
        tthis.countryoforigin = {
          value: tthis.templateJson.detailDeploy.countryoforigin.value,
          showFlag: tthis.templateJson.detailDeploy.countryoforigin.showFlag,
          showInput: tthis.templateJson.detailDeploy.countryoforigin.showInput
        };
        tthis.declarationunit = {
          value: tthis.templateJson.detailDeploy.declarationunit.value,
          showFlag: tthis.templateJson.detailDeploy.declarationunit.showFlag,
          showInput: tthis.templateJson.detailDeploy.declarationunit.showInput
        };
      })

    },
    getDetails(row) {
     this.row = row;
     this.showRow = true;
      this.$refs.TableToggle.clearSelection()
      this.$refs.TableToggle.toggleRowSelection(row);
    },
    SelectionChange(val, index) {
      console.log(val);
      console.log(index);
      this.multipleSelection = val;
    },
    handleSelectionChange(val) {
      console.log(val);
      this.multipleSelection1 = val;
    },
    handleDelete(index, row) {
      this.tableData.splice(index, 1);
    },
    handleDelete2(index, row) {
      this.gridData.splice(index, 1);
    },
    saveBatch(index, row) {
      this.tableData[this.showindex].remark1 = this.gridData;
      console.log(this.tableData);
      this.$message({
        message: "保存成功",
        type: "success"
      });
      this.save();
      this.dialogTableVisible = false;
    },
    // addBatch() {
    //   this.gridData.push({
    //     lottable01: "",
    //     qtyordered: ""
    //   });
    // },
    addBatch() {
      const objtemp = this.templateJson[this.detail.remark1]
      for(let prop in objtemp) {
        if(typeof objtemp[prop] == "object" &&  objtemp[prop].showFlag == true) {
            console.log(objtemp[prop]) 
            this.addJsonObj[prop] = ""
        }
      }
      console.log(this.addJsonObj);
      this.gridData.push(JSON.parse(JSON.stringify(this.addJsonObj)));
    },
    deltetGriddata() {
      if (this.multipleSelection.length < 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          this.gridData.splice(i, 1);
        }
      }
    },
    weightSplitting() {
      if(this.detailHead.spotservice01 || this.detailHead.totalnw && this.tableData.length > 0) {
         for(let i=0; i<this.tableData.length-1; i++) {
          this.tableData[i].totalnetweight  =  (this.tableData[i].expectedqty * this.detailHead.totalnw / this.sumTot).toFixed(2);  //拆分净重 
          this.tableData[i].totalgrossweight  =  (this.tableData[i].expectedqty * this.detailHead.spotservice01 / this.sumTot).toFixed(2)   //拆分毛重
          this.allnegihtTot = this.tableData[i].totalnetweight*1 + this.allnegihtTot*1;
          this.allweigthTot = this.tableData[i].totalgrossweight*1 + this.allweigthTot*1;
          console.log(this.allweigthTot);
         };
        
        //  console.log(this.tableData[this.tableData.length-1].totalnetweight = this.totalnw - allweigth);
         this.tableData[this.tableData.length-1].totalnetweight = (this.detailHead.totalnw  - this.allnegihtTot).toFixed(2);
         this.tableData[this.tableData.length-1].totalgrossweight = (this.detailHead.spotservice01 - this.allweigthTot).toFixed(2);
      }  else {
        this.$message({
            message: "请输入总净重或者总毛重",
            type: "error"
            })
      }},
    // render 事件
      renderHeader (h,{column}) { // h即为cerateElement的简写，具体可看vue官方文档
        return h(
          'div',{
            style:'line-height: 23px;'
          },
          [ 
            h('span', column.label),
            h('i', {
              class:'el-icon-edit',
              style:'color:#409eff;margin-left:10px; cursor: pointer;',
              on:{
                click: () => {
                  console.log(column);
                  this.dialogFormVisibleEdit = true;
                  this.formYwEdit = {
                    label : column.label,
                    value: column.property,
                    content : ""
                  }
                    
                }
              }
            })
          ],
        );
      },
      eEditYwSelect() {
        console.log(this.formYwEdit.value, this.formYwEdit.content);
     if (this.multipleSelection1.length < 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
          for(var i= 0; i<this.multipleSelection1.length; i++) {
              this.multipleSelection1[i][this.formYwEdit.value] = this.formYwEdit.content.replace(/^\s+|\s+$/g,"")
          }
          this.dialogFormVisibleEdit = false;
          this.save();
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
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=9`)
        .then(function(resp) {
          tthis.optionsCodeDeli = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=1`)
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
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=6`)
        .then(function(resp) {
          console.log(resp);
          tthis.optionsCountry = resp.data;
        });
      // this.$http
      // .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=4`)
      // .then(function(resp) {
      //   console.log(resp);
      //   tthis.optionsCust  = resp.data;
      // });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=8`)
        .then(function(resp) {
          tthis.optionsCodeTrans = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=17`)
        .then(function(resp) {
          tthis.optionsDuty = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=5`)
        .then(function(resp) {
          tthis.optionsCust1 = resp.data;
        });
       this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=11`)
        .then(function(resp) {
          tthis.optionsCutmode = resp.data;
        });
        this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=16`)
        .then(function(resp) {
          tthis.optionsTrade = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=2`)
        .then(function(resp) {
          tthis.optionsCodeCurr = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=4`)
        .then(function(resp) {
          tthis.optionsPort = resp.data; 
        });
    },
    remoteMethod(query) {
      const tthis = this;
      if (query !== "") {
        this.loading2 = true;
        setTimeout(() => {
          this.loading2 = false;
          this.$http
            .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=39`)
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
    openconfirm(message) {
        this.$alert(message, '提示', {
          dangerouslyUseHTMLString: true
        });
     },
    egis() {
      // this.save();
      // console.log(this.detailHead);
            // 表头验证的部分
      if(this.detailHead.transportation == "5" || this.detailHead.transportation == "2") {
        if(!this.detailHead.billoflading) {
           this.openconfirm("运单号不能为空")
           return;
        }
      }
      if(this.detailHead.billoflading) {
        if(this.detailHead.transportation !== "5" && this.detailHead.transportation !== "2") {
             this.openconfirm("运输方式不合法");
             return;
        }
        }
      if(this.detailHead.spotservice03) {
        if(this.detailHead.spotservice03 == "2225" || this.detailHead.spotservice03 == "2248") {
          if(this.detailHead.transportation !== "2") {
            this.openconfirm("运输方式不合法");
            return;
          }
          if(this.detailHead.spotservice02 !== "3") {
            this.openconfirm("征免方式不合法");
            return;
          }
        }
      }
     if(this.detailHead.spotservice03 == "2233" ||  this.detailHead.spotservice03 == "2244" ) {
        if(this.detailHead.transportation != "5") {
          this.openconfirm("运输方式必须为航空运输");
          return;
        }
                if(this.detailHead.spotservice02 != "3") {
          this.openconfirm("征免方式必须为全免[3]");
          return;
        }
          if(this.detailHead.spotservice04 != "1234") {
          this.openconfirm("监管方式必须为保税区仓储转口[1234]");
          return;
        }
      }
      if(this.detailHead.spotservice03 == "2225" ||  this.detailHead.spotservice03 == "2248" ) {
        if(this.detailHead.transportation != "2") {
          this.openconfirm("运输方式必须为航空运输");
          return;
        }
                if(this.detailHead.spotservice02 != "3") {
          this.openconfirm("征免方式必须为全免[3]");
          return;
        }
          if(this.detailHead.spotservice04 != "1234") {
          this.openconfirm("监管方式必须为保税区仓储转口[1234]");
          return;
        }
      }
      // 监管方式
      if(this.detailHead.spotservice04) {
          if(this.detailHead.spotservice04 == "1234" || this.detailHead.spotservice04 == "1200" || this.detailHead.spotservice04 == "0615" || this.detailHead.spotservice04 == "0214") {
              if(this.detailHead.spotservice02 != "3") {
                this.openconfirm("征免方式不合法");
                return;
              }
          }
                    switch (this.detailHead.spotservice04) {
                            case "0110":
                            if(this.detailHead.spotservice05 != "101") {
                              this.openconfirm("征免性质不合法(必须为--一般征税【101】)");
                              return;
                            }
                            if(this.detailHead.spotservice02 != "1") {
                              this.openconfirm("征免方式不合法(必须为-  照章征税征税【1】)");
                              return;
                            }
                            // console.log(this.detailHead.soreference2, 443453423)
                            // if(!this.detailHead.soreference2) {
                            //   this.openconfirm("当监管方式为一般贸易【0110】 时合同号必填");
                            //   return;
                            // }
                            break;
                            case "0615":
                            if(this.detailHead.spotservice05 != "503") {
                              this.openconfirm("征免性质不合法(必须为--进料加工【503】)");
                              return;
                            }
                              break;
                            case "0214":
                              if(this.detailHead.spotservice05 != "502") {
                              this.openconfirm("征免性质不合法(必须为--进料加工【502】)");
                              return;
                            }
                          }
      }
        for(let tableObj in this.tableData[0]) {
              for(let tempJsonObj in this.templateJson.detailDeploy) {
                if(tableObj == tempJsonObj && this.templateJson.detailDeploy[tempJsonObj].showInput == true && this.templateJson.detailDeploy[tempJsonObj].showFlag == false && this.templateJson.detailDeploy[tempJsonObj].required == true) {
                    for(let i=0 ; i<this.tableData.length; i++) { 
                      if(this.tableData[i].remark.sku != true) {
                          this.openconfirm("有未在商品库发现的料号 请检查");
                          return;
                        }
                      if(!this.tableData[i][tableObj]) {
                        this.openconfirm(`请检查第${i+1}条数据的${tempJsonObj}-- (不能为空)`);
                        return;
                      }

                                      // 验证hs不能为空
                      if(this.detail.remark1 == "C000014") {
                        const compareArr = guangLaiArr1();
                        const comarSo = guanglaiSo1();
                        if(compareArr.indexOf(this.tableData[i].lottable01) == -1) {
                            alert(`第${i+1}条数据的最终用户不正确（未在参数库中找到）`);
                            return;
                        } 
                        if(comarSo.indexOf(this.tableData[i].lottable01) !== -1 && !this.tableData[i].lottable02) {
                            alert(`第${i+1}条数据的SO号必须填写`);
                            return;
                        } 
                      }
                      console.log(tempJsonObj)
                    }
                }
                if(this.btnAdd == true && this.templateJson.detailDeploy[tempJsonObj].showInput == true && this.templateJson.detailDeploy[tempJsonObj].showFlag == true && this.templateJson.detailDeploy[tempJsonObj].required == true) {
                      for(let i=0 ; i<this.tableData.length; i++) {
                          for(let z = 0 ; z<this.tableData[i].remark1.length; z++ ) {
                                   if(!this.tableData[i].remark1[z][tempJsonObj] || this.tableData[i].remark1[z][tempJsonObj] == undefined) {
                                     this.openconfirm(`第${i+1}条数据的对多数据里面第${z+1}条数据 --${this.templateJson.detailDeploy[tempJsonObj].value}-- 不能有空`);
                                     return
                                   }
                         }
                        }
                    }
              }
            }
        this.$refs.detailHead.validate((valid) => {
          if (valid) {
            // if(this.detail.remark1 == "C000051" || this.detail.remark1 == "C000064") {
            //    console.log(this.detailHead.billoflading.substr(0, 6));
            //   if(this.detailHead.billoflading.substr(0, 6) == "HPHSHA" && this.detailHead.spotservice03 !== "2202") {
            //       this.openconfirm(`进出口岸必须为【2202】吴淞海关`);
            //       return
            //   }
            //     }
           if(this.tableData.length>0) {
             if(this.btnAdd == true) {
                for(let i=0; i<this.tableData.length; i++) {
                 var CARTONQTY = 0; // batch数量 
                 var D_USERDEFINE2 = 0 // 分金额
                  var currList = []; 
                   var countryoforiginList = [];
                 if(!this.tableData[i].price) {
                   this.openconfirm("请先计算单价");
                   return;
                 }
                  if((this.tableData[i].price.toFixed(3)*1 - (this.tableData[i].totalprice*1 / this.tableData[i].openqty*1).toFixed(3)*1).toFixed(3)*1 > 0.001 ||  ((this.tableData[i].totalprice*1 / this.tableData[i].openqty*1).toFixed(3)*1).toFixed(3)*1 -this.tableData[i].price.toFixed(3)*1  > 0.001) {
                    this.openconfirm(`第${i+1}条数据的单价与总价和数量的比值误差大于正负0.001`);
                    return;
                  }
                 for(let c=0; c<this.optionsCodeCurr.length; c++) {
                    currList.push(this.optionsCodeCurr[c].ncode)
                  }
                  if(currList.indexOf(this.tableData[i].currency) == -1) {
                    this.openconfirm(`请检查第${i+1}条币制`);
                    return false;
                  }
                 if(this.countryoforigin.showInput == true) {
                   for(let i=0; i<this.optionsCountry.length; i++) {
                      countryoforiginList.push(this.optionsCountry[i].ncode);
                    }
                    if(countryoforiginList.indexOf(this.tableData[i].countryoforigin) == -1) {
                    this.openconfirm(`请检查第${i+1}条的原产国`);
                    return false;
                  }
                }
                 const reg = /^\d{8}$/;
                 if(!reg.test(this.tableData[i].sku*1) && this.detail.remark1 == "C000002") {
                   this.openconfirm(`第${i+1}条料号不合法`);
                   return;
                 }
                if(this.tableData[i].remark1 && this.tableData[i].remark1.length > 0) {
                  for(let k= 0; k<  this.tableData[i].remark1.length ; k++) {
                    if(JSON.stringify(this.tableData[i].remark1[k]) == '{}') {
                      this.openconfirm(`请录入第${i+1}条数据的弟${k}条数据`);
                      return;
                    } 
                     CARTONQTY += this.tableData[i].remark1[k].qtyordered*1;
                     D_USERDEFINE2 += this.tableData[i].remark1[k].volumn*1
                   if(this.detail.remark1 == "C000047" || this.detail.remark1 == "C000065" ) {
                    console.log(this.tableData[i].remark1[k].lottable01)
                    if(this.tableData[i].remark1[k].lottable01.length != 5 || isNaN(Number(this.tableData[i].remark1[k].lottable01))) {
                      this.openconfirm(`第${i+1}条的数据里面的第${k+1}行箱号必须为五位数字`);
                      return;
                    }
                  }
                  }
                }else {
                  this.openconfirm(`请录入第${i}条数据的batch`)
                } 
                 if(this.qtyordered.showInput == true ) {
                  if( this.tableData[i].openqty*1 !== CARTONQTY*1 && this.detail.remark1 !== "C000002") {
                    this.openconfirm(`请检查第${i+1}条数据的batch数量`)
                    return false
                  }
                   }
                if(this.volumn.showInput == true) {
                    console.log((this.tableData[i].totalprice*1).toFixed(5) , (D_USERDEFINE2*1).toFixed(5))
                  if( (this.tableData[i].totalprice*1).toFixed(5) !== (D_USERDEFINE2*1).toFixed(5)) {
                    this.openconfirm(`请检查第${i+1}条数据的分金额`)
                    return false
                  }
                }
              }
              const tthis = this;
                  this.$http
                    .post(
                      `${process.env.VUE_APP_BASE_API}kwe/index/audit?wlywno=${
                        this.detail.ywNo
                      }&status=1&op_user=${this.session}`
                    )
                    .then(resp => {
                      if (resp.data == "updateNice") {
                        tthis.$message({
                          message: "更改状态成功",
                          type: "success"
                        });
                        tthis.searchDetail();
                      } else {
                        tthis.$message({
                          message: "更改状态失败",
                          type: "error"
                        });
                      }
                    });
                   } else {
                    for(let i=0; i<this.tableData.length; i++) {
                   var currList = []; 
                   var countryoforiginList = [];
                  for(let c=0; c<this.optionsCodeCurr.length; c++) {
                    currList.push(this.optionsCodeCurr[c].ncode)
                  }
                  if(currList.indexOf(this.tableData[i].currency) == -1) {
                    this.openconfirm(`请检查第${i+1}条币制`);
                    return false;
                  }
                 if(this.countryoforigin.showInput == true) {
                   for(let i=0; i<this.optionsCountry.length; i++) {
                      countryoforiginList.push(this.optionsCountry[i].ncode);
                    }
                    if(countryoforiginList.indexOf(this.tableData[i].countryoforigin) == -1) {
                    this.openconfirm(`请检查第${i+1}条的原产国`);
                    return false;
                  }
                }
                    if(!this.tableData[i].price) {
                      this.openconfirm("请先保存计算单价");
                      return;
                    }
                  if((this.tableData[i].price.toFixed(3)*1 - (this.tableData[i].totalprice*1 / this.tableData[i].openqty*1).toFixed(3)*1).toFixed(1)*1 > 0.001 ||  ((this.tableData[i].totalprice*1 / this.tableData[i].openqty*1).toFixed(3)*1).toFixed(3)*1 -this.tableData[i].price.toFixed(3)*1  > 0.001) {
                    this.openconfirm(`第${i+1}条数据的单价与总价和数量的比值误差大于正负0.001`);
                    return;
                  }
                    console.log( this.tableData[i].sku.substr(0,this.tableData[i].sku.length-1))
                    // if(this.detail.remark1 == "C000016" && ( this.tableData[i].sku.substr(0,this.tableData[i].sku.length-1).indexOf('o') !== -1 || this.tableData[i].sku.substr(this.tableData[i].sku.length-1,1).indexOf('0') !== -1)) {
                    //   this.openconfirm(`第${i+1}条料号不合法`);
                    //   return;
                    // }
                  }
                 const tthis = this;
                  this.$http
                    .post(
                      `${process.env.VUE_APP_BASE_API}kwe/index/audit?wlywno=${
                        this.detail.ywNo
                      }&status=1&op_user=${this.session}`
                    )
                    .then(resp => {
                      if (resp.data == "updateNice") {
                        tthis.$message({
                          message: "更改状态成功",
                          type: "success"
                        });
                        tthis.searchDetail();
                      } else {
                        tthis.$message({
                          message: "更改状态失败",
                          type: "error"
                        });
                      }
                    });
             }
            } else{
              this.openconfirm("请先录入表体数据");
            }
          } else {
            console.log('error submit!!');
            return false;
          }
        });
    },
    egisFail() {
      if(this.detailHead.status == 2) {
        this.verifyStart = "待审核"
      }
      if(this.detailHead.status == 3) {
        this.verifyStart = "待修改"
      }
      const tthis = this;
      this.$http
        .post(`${process.env.VUE_APP_BASE_API}kwe/index/audit?wlywno=${this.detail.ywNo}&status=0&op_user=${this.session}`)
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
              .get("${process.env.VUE_APP_BASE_API}kwe/serialno/getMessageHead")
              .then(function(res) {
                tthis.o[item].forEach(resp => {
                  resp.messagehead = res.data;
                  tthis.tempArr.push(tthis.o[item][0].messagehead);
                });
              });
          } else {
            this.$http
              .get("${process.env.VUE_APP_BASE_API}kwe/serialno/getMessageHead")
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
    exportExcel() {
      const _this = this;
           this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/download?ywNo=${this.detail.ywNo}`)
        .then(function(resp) {
          if(resp.headers.status !== "200") {
            _this.$message({
              message: "下载失败",
              type: "error"
            })
          } else {
              location.href = `${process.env.VUE_APP_BASE_API}kwe/index/download?ywNo=${_this.detail.ywNo}`;
          }
        });
  },
    priceSave(price) {
            for (let i = 0; i < this.tableData.length; i++) {
              if(this.tableData[i].price == 'NaN')  {
               this.openconfirm(`请检查第${i+1}条数据是否有误`);
               return false;
              }
              this.tableData[i].price = ((this.tableData[i].totalprice * 1) / this.tableData[i].openqty *1).toFixed(price);
        }
    },
    async synchroTo() {
                      const tthis = this;
                      this.$http.post(`${process.env.VUE_APP_BASE_API}/dict/insertByDetail`, {
                        user: this.session,
                        customKey: this.detail.remark1,
                        selectList: this.tableData
                      }).then(resp=>{
                        if(resp.data.status == "200") {
                          tthis.$message({
                            message: resp.data.message,
                            type: "success"
                          });
                          console.log(resp);
                          tthis.searchDetail();
                        } else {
                          tthis.$message({
                            message: resp.data.message,
                            type: "error"
                          });
                        }
                      })
    },
    save() {
      this.detailHead.op_user = this.session;
      if (this.$route.query.status == "未录入") {
        let tthis = this;
        const detailHead = [];
        detailHead.push(this.detailHead);
        const row = [];
        const allMessage = [];
        const data = {
          outputHeadList: detailHead,
          outputDetailList: this.tableData,
          op_user: this.session
        };
        this.$http
          .post(`${process.env.VUE_APP_BASE_API}kwe/index/outputInsert`, data)
          .then(function(resp) {
            if (resp.data == "saveNice") {
              tthis.$message({
                message: "保存成功",
                type: "success"
              });
          tthis.searchDetail();
                     tthis.$router.push({
            query:merge(tthis.$route.query,{'status':'待审核'})
        })
              tthis.searchDetail();
            } else {
              tthis.$message({
                message: "保存失败",
                type: "error"
              });
            }
          }).catch(function (error) {
                tthis.$message({
                showClose: true,
                duration:0,
                message: error,
                type: "error"
              });
              });
      } else {
        console.log("this data");
        let tthis = this;
        // this.uniq(this.dnNumber).forEach(resp => {
        //   headDate.push(JSON.parse(JSON.stringify(this.detailHead)));
        // });
        const row = [];
        for (var i = 0; i < this.tableData.length; i++) {
          this.tableData[i].messagehead = this.detailHead.messagehead;
          this.tableData[i].sku = this.tableData[i].sku.replace(/[‑—~]/,"-"); 
          // 尼吉康
          if(this.detail.remark1 == "C000067" ) {
            this.tableData[i].totalprice = this.tableData[i].totalprice.toFixed(3)
          }
          if((this.detail.remark1 == "C000047" || this.detail.remark1 == "C000065")  && this.tableData[i].totalprice) {
                this.tableData[i].totalprice = (this.tableData[i].totalprice*1).toFixed(2);
                for(let b=0; b<this.tableData[i].remark1.length; b++) {
                  if(this.tableData[i].remark1.volumn) {
                    this.tableData[i].remark1.volumn = (this.tableData[i].remark1.volumn*1).toFixed(2)
                  }
                }
          }
          if(this.detail.remark1 == "C000016") {
            this.tableData[i].sku = this.tableData[i].sku.substr(0,this.tableData[i].sku.length-1).replace('O','0') + this.tableData[i].sku.substr(this.tableData[i].sku.length-1,1).replace('0', 'O');
            this.tableData[i].volumnuom = this.tableData[i].volumnuom.substr(0,this.tableData[i].volumnuom.length-1) + this.tableData[i].volumnuom.substr(this.tableData[i].volumnuom.length-1,1).replace('0', 'C');
          }
          this.tableData[i].customerlineno = this.tableData[i].customerlineno.replace(/[‑—~]/,"-");
          // if(this.detail.remark1 == "C000037" ) {
          //   this.tableData[i].sku = this.tableData[i].sku.replace(/\s*/g,"");
          // }
          // 斯坦雷电气
          if(this.detail.remark1 == "C000018" ) {
            this.tableData[i].customerlineno = this.tableData[i].customerlineno.replace(/\s*/g,"");
            // this.tableData[i].sku = this.tableData[i].sku.replace("O", "0");
            this.tableData[i].sku = this.tableData[i].sku.replace("PR0", "PRO")
            this.tableData[i].sku = this.tableData[i].sku.replace("PR0T0", "PROTO");
            this.tableData[i].sku = this.tableData[i].sku.replace("PROT0", "PROTO");
            this.tableData[i].sku = this.tableData[i].sku.replace("KS", "K3");
            this.tableData[i].sku = this.tableData[i].sku.replace("M0DULE", "MODULE");
            if(this.tableData[i].sku.indexOf("TR") !== -1 && this.tableData[i].sku.substr(this.tableData[i].sku.length-1,1) == '0') {
              this.tableData[i].sku = this.tableData[i].sku.replace(this.tableData[i].sku.charAt(this.tableData[i].sku.length-1)+"","O");
            }
            // if(this.tableData[i].sku.indexOf("I") !== -1) {
            //   this.tableData[i].sku = this.tableData[i].sku.replace("I", "1")
            // }
          }
          if(this.detail.remark1 == "C000047" || this.detail.remark1 == "C000065") {
            this.tableData[i].sku = this.tableData[i].sku.replace("ff","W");
          }
          if(this.detail.remark1 == "C000047") {
            this.tableData[i].sku = this.tableData[i].sku.replace("HTFP","HJFP");
          }
          if(this.detail.remark1 == "C000067" && this.tableData[i].totalprice) {
            this.tableData[i].totalprice = this.tableData[i].totalprice.toFixed(3);
          }
          // 田村
          if(this.detail.remark1 == "C000004") {
            if(this.tableData[i].sku.substr(this.tableData[i].sku.length-2, 2) == "AR") {
              this.tableData[i].sku = this.tableData[i].sku.substr(0, this.tableData[i].sku.length-2);
            }
          }
                    // 罗门哈斯
          if(this.detail.remark1 == "C000021") {
            if(this.tableData[i].sku.substr(0, 3) == "000") {
              this.tableData[i].sku = this.tableData[i].sku.substr(3, this.tableData[i].sku.length);
            }
          }
                    // GD
          if(this.detail.remark1 == "C000002") {
             if(this.tableData[i].customerlineno.substr(0, 1) == "O") {
              this.tableData[i].customerlineno = "0" + this.tableData[i].customerlineno.substr(1, this.tableData[i].customerlineno.length);
            }
          }
                  // 豪熙电
          if(this.detail.remark1 == "C000054") { 
              this.tableData[i].customerlineno = this.tableData[i].customerlineno.replace("H0S", "HOS")
          }
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
          .post(`${process.env.VUE_APP_BASE_API}kwe/index/outputUpdate`, data)
          .then(function(resp) {
            if (resp.data == "updateNice") {
              tthis.$message({
                message: "修改成功",
                type: "success"
              });
              if(tthis.detail.status == "已识别待补充") {
                  tthis.$router.push({
                      query: merge(tthis.$route.query, { status: "待审核" })
                   });
              }
              tthis.searchDetail();
            } else {
              tthis.$message({
                message: "修改失败",
                type: "error"
              });
            }
          }).catch(function (error) {
                tthis.$message({
                showClose: true,
                duration:0,
                message: error,
                type: "error"
              });
              });
      }
    },
    colorChange (val) {
      // return val.length
      if(this.detail.remark1 == "C000002"){
          const reg = /^\d{8}$/; 
            if (reg.test(val*1)) {
              return false
            } else {
              return true
            }
      } else {
        return false;
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
          sums[index] = sums[index].toFixed(5) + "数量";
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
        cubic: "",
        remark:{
          chhinesename: false,
          countryoforigin: false,
          currency: false,
          declarationunit: false,
          hscode: false,
          netweight: false,
          price: false,
          sku: false
        }
      });
    },
        dataBaseImport(event) {

            const tthis = this;
            const data = {
              ywNo: this.detail.ywNo,
              excel: this.file[0]
            }

            event.preventDefault();
            let formData = new FormData();
            formData.append('ywNo', this.detail.ywNo);
            formData.append('excel', this.form.file[0]);
            let config = {
              headers: {
                'Content-Type': 'multipart/form-data'
              } 
            }
        this.$http
        .post(
          `${process.env.VUE_APP_BASE_API}/kwe/index/uploadExcel`, formData,config
        )
        .then(resp => {
          if (resp.data.status == "200") {
            tthis.$message({
              message: "上传成功",
              type: "success"
            });
            tthis.form.file = [];
            tthis.searchDetail();
            tthis.dialogFormVisible = false;
          } else {
            tthis.$message({
              message: resp.data.message,
              type: "error"
            });
          }
        }).catch(function (error) {
                tthis.$message({
                showClose: true,
                duration:0,
                message: error,
                type: "error"
              });
              });
    },
    successUpload (file) {
      // 上传成功在表单的某个字段里加一个值
        this.form.file.push(file.raw);
        // this.form.file.push(file.response.result[0].url);
    },
     beforeAvatarUpload(file) {
        let Xls = file.name.split('.');
        if(Xls[1] === 'xls'||Xls[1] === 'xlsx'){
          return file
        }else {
          this.$message.error('上传文件只能是 xls/xlsx 格式!')
          return false
        }
      },
          getSummariesTable(param) {
               const { columns, data } = param;
        const sums = [];
        columns.forEach((column, index) => {
          if (index === 0) {
            sums[index] = '总计';
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
            if(index == 5 || index == 7 || index == 8) {
              sums[index] = ' ' + sums[index].toFixed(2) ;
            } else {
              sums[index] = ''
            }
          } else {
            sums[index] = '';
          }
        });

        return sums;
      
    },
      Remove(file, fileList) {
          this.form.file = [];
      },
      tirggerFile(event) {
          this.file = event.target.files;
      },
      tableRowClassName ({row, rowIndex}) {
        //把每一行的索引放进row
       row.index = rowIndex;
    },
        keyCodeNext(event) {
    const inputs = document.querySelectorAll('input');
    for(let i=0; i<inputs.length; i++) {
      if(inputs[i] == event.srcElement)  {
        inputs[i+1].focus();
      }
    }
    },
    copylist() {
      this.$refs.TableToggle.store.states.selection;
      if(this.multipleSelection1.length != 1) {
               this.$message({
                      message: "请选择一条数组操作",
                      type: "error"
                    })
      } else {
        const lineData = JSON.parse(JSON.stringify(this.multipleSelection1[0]));
        lineData.pid = "";
          this.tableData.push(
           lineData
          )
      }
    },
    modifyTable() {
       const tthis = this;
      if (this.multipleSelection1.length < 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
        console.log(this.multipleSelection1);
      }
    },
    dignetweight1out(splitOne, split2) {
      this.loading = true;
      for(let i=0; i<this.tableData.length; i++){
            var totBatch = 0;
            var netweight1 = 0;
        if(this.tableData[i].remark1.length > 0) {
                  for(let k= 0; k<this.tableData[i].remark1.length; k++) {
                      totBatch = this.tableData[i].remark1[k].qtyordered*1 + totBatch;  
                    }
                    console.log(totBatch);
                    for(let z= 0; z<this.tableData[i].remark1.length-1; z++) {
                      if(this.detail.remark1 != "C000047") {
                            this.tableData[i].remark1[z][split2]  =  (this.tableData[i][splitOne]*1 * this.tableData[i].remark1[z].qtyordered*1 / totBatch).toFixed(3);  //拆分净重  
                            netweight1 = this.tableData[i].remark1[z][split2]*1 + (netweight1.toFixed(2))*1;
                      } else {
                            this.tableData[i].remark1[z][split2]  =  (this.tableData[i][splitOne]*1 * this.tableData[i].remark1[z].qtyordered*1 / totBatch).toFixed(2);  //拆分净重  
                            netweight1 = this.tableData[i].remark1[z][split2]*1 + (netweight1.toFixed(2))*1;
                      }
                    }

        }
        if(this.detail.remark1 != "C000047") { 
           this.tableData[i].remark1[this.tableData[i].remark1.length-1][split2] = (this.tableData[i][splitOne]*1  - netweight1).toFixed(3);
        } else {
          this.tableData[i].remark1[this.tableData[i].remark1.length-1][split2] = (this.tableData[i][splitOne]*1  - netweight1).toFixed(2);
        }
      }
      this.loading = false;
    },
    digMoney() {
      var totBatch = 0;
      var volumn = 0;
      if(this.row.remark1.length > 0) {
        for(let i= 0; i<this.row.remark1.length; i++) {
          totBatch = this.row.remark1[i].qtyordered*1 + totBatch;
        }
       for(let i= 0; i<this.row.remark1.length-1; i++) {
         if(this.detail.remark1 != "C000047") {
              this.row.remark1[i].volumn  =  (this.row.totalprice*1 * this.row.remark1[i].qtyordered / totBatch).toFixed(3)   //拆分金额
              volumn = this.row.remark1[i].volumn*1 + volumn;
          } else {
              this.row.remark1[i].volumn  =  (this.row.totalprice*1 * this.row.remark1[i].qtyordered / totBatch).toFixed(2)   //拆分金额
              volumn = this.row.remark1[i].volumn*1 + volumn;
          }
       }
        if(this.detail.remark1 != "C000047") {
            this.row.remark1[this.row.remark1.length-1].volumn = (this.row.totalprice*1 - volumn).toFixed(3);
        } else {
            this.row.remark1[this.row.remark1.length-1].volumn = (this.row.totalprice*1 - volumn).toFixed(2);
        }
         
      }
    },

    digMoneyout(splitOne, split2) {
      this.loading = true;
      for(let i=0; i<this.tableData.length; i++){
            var totBatch = 0;
            var netweight1 = 0;
        if(this.tableData[i].remark1.length > 0) {
                  for(let k= 0; k<this.tableData[i].remark1.length; k++) {
                      totBatch = this.tableData[i].remark1[k].qtyordered*1 + totBatch;
                    }
                    for(let z= 0; z<this.tableData[i].remark1.length-1; z++) {
                          this.tableData[i].remark1[z][splitOne]  =  (this.tableData[i].remark1[z][split2]*1 * this.tableData[i].remark1[z].qtyordered / totBatch).toFixed(5);  //拆分净重  
                            netweight1 = this.tableData[i].remark1[z][splitOne]*1 + netweight1;
                    }
        }
       this.tableData[i].remark1[this.tableData[i].remark1.length-1][split2] = (this.tableData[i][split2]*1  - netweight1).toFixed(5);
      }
      this.loading = false;
    },
    calculatePrice() {
        this.tableData.forEach(res=>{
          if(!res.totalprice) {
            console.log(res.openqty, res.price)
            res.totalprice = (Number(res.openqty) * Number(res.price)).toFixed(3)
          }
        })
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
.el-table__footer-wrapper tbody td,
.el-table__header-wrapper tbody td {
  color: seagreen !important;
} 
.messageIndex{
  font-size: 14px;
}
.messageIndexIn{
  font-size: 16px;
  color: red;
}
</style>


