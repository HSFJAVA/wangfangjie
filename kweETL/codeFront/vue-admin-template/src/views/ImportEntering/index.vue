<template>
  <div class="expotrEnt app-container" v-loading="loading" element-loading-text="拼命加载中" @keyup.enter="keyCodeNext">
    <div>
      <el-button type="primary" size="mini" @click="save()" :disabled="disabledBtn" plain>保存</el-button>
      <el-button
        type="primary"
        size="mini"
          @click="dialogFormVisible = true"
        plain
      >数据导入</el-button>
      <!-- <el-button type="primary" size="mini" @click="weightSplitting()"  plain>重量拆分</el-button> -->
      <el-dropdown>
      <el-button type="primary" size="mini" plain>
        重量操作<i class="el-icon-arrow-down el-icon--right"></i>
      </el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="weightSplitting()">净重，毛重拆分（根据数量拆分）</el-dropdown-item>
        <el-dropdown-item  @click.native="SplittingGross()">净重拆分（根据毛重）</el-dropdown-item>
        <el-dropdown-item @click.native="SplittingNet()" >毛重拆分（根据净重）</el-dropdown-item>
        <el-dropdown-item @click.native="voluaNetGross()" >赋值净毛重（根据分净，毛重）</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
      <el-button type="primary" size="small" plain @click="exportExcel()">导出Excel</el-button>
      <el-button type="success" size="mini" :disabled="disabledBtn" @click="egis()" plain>审核通过</el-button>
      <el-button type="danger" size="mini" @click="egisFail()" :disabled="disabledBtn2" plain>驳回</el-button>
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
      <el-form label-width="80px" size="mini" :model="detailHead" :rules="rules" ref="detailHead">
        <!-- 第一行  -->
        <el-row :gutter="20">
          <el-col :span="6" size="mini">
            <el-form-item label-width="120px" label="唯一编号">
              <el-input v-model="detail.ywNo" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="业务编号">
              <el-input v-model="detail.customNo" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="货主ID">
              <el-input v-model="detailHead.storeid" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="单证类型">
              <el-input v-model="detail.type" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label-width="120px" label="更新标准">
              <el-input v-model="detailHead.updateflag" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="进出口岸" prop="spotservice03">
              <el-select ref="mySelect" clearable
                v-model="detailHead.spotservice03"
                filterable
                placeholder="请选择"
                @change="$set(detailHead, detailHead.spotservice03, $event)"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCust1"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label-width="120px" label="入境口岸" v-if="placeofdischarge.showInput == true" prop="placeofdischarge">
              <el-select ref="mySelect" clearable
                v-model="detailHead.placeofdischarge"
                filterable
                placeholder="请选择"
                @change="$set(detailHead, detailHead.placeofdischarge, $event)"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsinPort"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
           <el-col :span="6" >
            <el-form-item label-width="120px" label="启运日期"  v-if="vehicledate.showInput == true"  prop="vehicledate">
                  <el-date-picker
                  style="width:100%"
                  v-model="detailHead.vehicledate"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                  type="date"
                  placeholder="选择日期">
                    </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6" >
            <el-form-item label-width="120px" label="境外收发货人"  v-if="shippername.showInput == true"  prop="shippername">
              <el-input v-model="detailHead.shippername"></el-input>
            </el-form-item>
          </el-col> 
          <el-col :span="6" >
            <el-form-item label-width="120px" label="供应商"  v-if="shipperid.showInput == true"  prop="shipperid">
              <el-input v-model="detailHead.shipperid"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" >
            <el-form-item label-width="120px" label="运输方式"  prop="transitmode">
              <!-- <el-input v-model="detailHead.transitmode"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model.trim="detailHead.transitmode"
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
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label-width="120px" label="运单号" prop="billoflading"  v-if="billoflading.showInput == true">
              <el-input v-model.trim="detailHead.billoflading"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="船名/航次" prop="vessel"  v-if="vessel.showInput == true">
              <el-input v-model.trim="detailHead.vessel"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="合同协议号" prop="asnreference1"  v-if="asnreference1.showInput == true">
              <el-input v-model.trim="detailHead.asnreference1"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="countryoforigin1.showInput == true">
            <el-form-item label-width="120px" label="贸易国别(地区)" prop="countryoforigin">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.countryoforigin"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCountry"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']'  + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="监管方式" prop="spotservice04">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.spotservice04"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsTrade"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="spotservice051.showInput == true">
            <el-form-item label-width="120px" label="征免性质" prop="spotservice05">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.spotservice05"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCutmode"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="征免方式" prop="spotservice02">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.spotservice02"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsDuty"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="成交方式" style="width:100%" prop="deliveryterms">
              <!-- <el-input v-model="detailHead.deliveryterms"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.deliveryterms"
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
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="启运国" prop="placeofloading">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.placeofloading"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCountry"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']'  + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="经停港" prop="placeofdelivery">
              <el-select ref="mySelect" clearable
                v-model="detailHead.placeofdelivery"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsPort"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']' + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select ref="mySelect">
              <!-- <el-input v-model="detailHead.placeofdelivery"></el-input> -->
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="目的国" prop="countryofdestination">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.countryofdestination"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCountry"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']'+  '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="总件数" prop="containerqty">
              <el-input v-model="detailHead.containerqty"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="containertype.showInput == true">
            <el-form-item label-width="120px" label="包装种类" prop="containertype">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select ref="mySelect" clearable
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
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="总毛重"  prop="spotservice01">
              <el-input v-model.trim="detailHead.spotservice01" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" >
            <el-form-item label-width="120px" label="总净重" v-if="totalnw.showInput == true"   prop="totalnw">
              <el-input v-model.trim="detailHead.totalnw" ></el-input>
            </el-form-item>
          </el-col>
                    <el-col :span="6" >
            <el-form-item label-width="120px" label="关联报关单号" v-if="asnreference2.showInput == true"   prop="asnreference2">
              <el-input v-model.trim="detailHead.asnreference2" ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <div class="newBtnlist">
      <el-button round size="mini" @click="addnewFormlist()">新增</el-button>
      <el-button round size="mini" @click="copylist()">复制</el-button>
      <el-dropdown>
          <el-button  round size="mini">
            拆分（batch相关）<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="dignetweight1out( 'totalnetweight' ,'netweight', 4)">分净重拆分</el-dropdown-item>
            <el-dropdown-item @click.native="dignetweight1out( 'totalgrossweight' ,'grossweight', 2)">分毛重拆分</el-dropdown-item>
            <el-dropdown-item v-if="detail.remark1 == 'C000047'" @click.native="dignetweight1out( 'totalprice' ,'d_userdefine2', 2)">分金额拆分</el-dropdown-item>
            <el-dropdown-item v-if="detail.remark1 != 'C000047'" @click.native="dignetweight1out( 'totalprice' ,'d_userdefine2', 3)">分金额拆分</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

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
        border
        :summary-method="getSummariesTable"
        show-summary
        ref="TableToggle"
        @row-click="getDetails"
        :row-class-name="tableRowClassName"
        :highlight-current-row="true"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column type="index" width="50" fixed></el-table-column>
        <el-table-column label="操作" width="160" fixed>
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            <el-button
              v-if="btnAdd == true"
              size="mini"
              @click="openBanchDig(scope.$index, scope.row)"
            >添加</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="customerlineno" label="发票号" width="160" :render-header="renderHeader"></el-table-column>
        
        <el-table-column prop="sku" label="料号" width="180" :render-header="renderHeader">
          <template slot-scope="scope">
            <div :style="{color: scope.row.remark.sku == 0 ? '#F56C6C' : 'green'}">
              {{scope.row.sku}}
            </div>
          </template>
        </el-table-column>

        <!-- <el-table-column width="160" v-if="lottable06.showFlag == true"   prop="lottable01" :label="lottable06.value"></el-table-column>
        <el-table-column width="160" v-if="lottable07.showFlag == true"   prop="lottable01" :label="lottable07.value"></el-table-column>
        <el-table-column width="160" v-if="lottable08.showFlag == true"   prop="lottable01" :label="lottable08.value"></el-table-column>
        <el-table-column width="160" v-if="lottable09.showFlag == true"   prop="lottable01" :label="lottable09.value"></el-table-column>
        <el-table-column width="160" v-if="lottable10.showFlag == true"   prop="lottable01" :label="lottable10.value"></el-table-column>
        <el-table-column width="160" v-if="lottable11.showFlag == true"   prop="lottable01" :label="lottable11.value"></el-table-column>
        <el-table-column width="160" v-if="lottable11.showFlag == true"   prop="lottable01" :label="lottable12.value"></el-table-column>-->

        <el-table-column prop="expectedqty" label="数量" width="160" :render-header="renderHeader"></el-table-column>
        <el-table-column prop="declarationunit" :render-header="renderHeader" :label="declarationunit.value" width="160" v-if="declarationunit.showInput == true"></el-table-column>
        <el-table-column prop="price" label="单价" :render-header="renderHeader" width="160"></el-table-column>
        <el-table-column prop="totalprice" label="总价" :render-header="renderHeader" width="160"></el-table-column>
        <el-table-column prop="currency" label="币制" :render-header="renderHeader" width="160">
           <!-- <template slot-scope="scope">
            <div :style="{color: currChange(scope.row.currency) ? '#F56C6C' : ''}">
              {{scope.row.currency}}
            </div>
          </template> -->
        </el-table-column>
        <el-table-column prop="countryoforigin" :render-header="renderHeader" label="原产国" width="160" v-if="countryoforigin.showInput == true"></el-table-column>
        <el-table-column prop="totalnetweight" :render-header="renderHeader" label="净重"width="160"></el-table-column> 
        <el-table-column prop="totalgrossweight" :render-header="renderHeader" label="毛重" width="160"></el-table-column>
        <!-- <el-table-column prop="totalcubic" label="体积"width="160"></el-table-column> -->
         <el-table-column prop="hscode" :render-header="renderHeader"  width="180" :label="hscode.value" v-if="hscode.showInput == true"></el-table-column>
        <el-table-column prop="chinesename" :render-header="renderHeader" label="品名" width="180" :label="chinesename.value"  v-if="chinesename.showInput == true" ></el-table-column>

        <!-- 显示隐藏部分 -->

        <el-table-column
          width="160"
          v-if="lottable01.showInput == true && lottable01.showFlag == false"
          prop="lottable01"
          :label="lottable01.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="160"
           v-if="lottable02.showInput == true && lottable02.showFlag == false"
          prop="lottable02"
          :label="lottable02.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="160"
          v-if="lottable03.showInput == true && lottable03.showFlag == false"
          prop="lottable03"
          :label="lottable03.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="160"
          v-if="lottable04.showInput == true && lottable04.showFlag == false"
          prop="lottable04"
          :label="lottable04.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="160"
          v-if="lottable05.showInput == true && lottable05.showFlag == false"
          prop="lottable05"
          :label="lottable05.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column
          width="160"
          v-if="doclineno.showInput == true && doclineno.showFlag == false"
          prop="doclineno"
          :label="doclineno.value"
          :render-header="renderHeader"
        ></el-table-column>
        <el-table-column :render-header="renderHeader" width="160" v-if="pono.showInput == true && pono.showFlag == false"  prop="pono" :label="pono.value"></el-table-column>
      </el-table>
    </div>
    <div>
      <el-form label-width="80px" size="mini" v-if="showRow">
        <h3>关务信息 <span class="messageIndex">(第 <span class="messageIndexIn">{{row.index+1}}</span> 行的信息)</span></h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label-width="120px" label="发票号">
              <el-input v-model.trim="row.customerlineno"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label-width="120px" label="料号">
              <el-input v-model.trim="row.sku"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6"  v-if="hscode.showInput == true">
            <el-form-item label-width="120px"  :label="hscode.value">
              <el-input v-model.trim="row.hscode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6"  v-if="chinesename.showInput == true">
            <el-form-item label-width="120px" :label="chinesename.value">
              <el-input v-model.trim="row.chinesename"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第一行  -->
        <el-row :gutter="20">
            <el-col :span="6">
            <el-form-item label-width="120px" label="数量">
              <el-input v-model.trim="row.expectedqty"></el-input>
            </el-form-item>
          </el-col>
           <el-col :span="6"  v-if="declarationunit.showInput == true ">
            <el-form-item label-width="120px"  :label="declarationunit.value">
              <el-input v-model.trim="row.declarationunit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="单价">
              <el-input v-model.trim="row.price"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="总价">
              <el-input @keyup.enter.native="submit(row)" v-model.trim="row.totalprice"></el-input>
            </el-form-item>
          </el-col>

        </el-row>
       <el-row :gutter="20">
            <el-col :span="6">
            <el-form-item label-width="120px" label="币制">
              <!-- <el-input v-model.trim="row.currency"></el-input> -->
              <el-select ref="mySelect" clearable v-model="row.currency" filterable placeholder="请选择" style="width:100%">
                <el-option
                  v-for="item in optionsCodeCurr"
                  :key="item.ncode"
                  :label="item.customsName + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="原产国" v-if="countryoforigin.showInput == true">
              <el-select ref="mySelect" clearable 
                v-model="row.countryoforigin"
                filterable
                placeholder="请选择"
                style="width:100%; overflow:hidden"
              >
                <el-option
                  v-for="item in optionsCountry"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']' + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="净重">
              <el-input v-model.trim="row.totalnetweight"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" label="毛重">
              <el-input v-model.trim="row.totalgrossweight"></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="6">
            <el-form-item label-width="120px" label="体积">
              <el-input v-model.trim="row.totalcubic"></el-input>
            </el-form-item>
          </el-col> -->
           
       
        </el-row>



        <h3>仓储信息</h3>
        <!-- 第三行 -->
        <el-row :gutter="20">
          <!-- 显示隐藏部分 -->
          <el-col :span="6">
            <el-form-item label-width="120px"
              :label="lottable01.value"
              v-if="lottable01.showInput == true && lottable01.showFlag == false"
              
            >
              <el-input v-model.trim="row.lottable01"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px"
              :label="lottable02.value"
              v-if="lottable02.showInput == true && lottable02.showFlag == false" 
              
            >
              <el-input v-model.trim="row.lottable02"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px"
              :label="lottable03.value"
              v-if="lottable03.showInput == true && lottable03.showFlag == false" 
              
            >
              <el-input v-model.trim="row.lottable03"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px"
              :label="lottable04.value"
              v-if="lottable04.showInput == true && lottable04.showFlag == false"
              
            >
              <el-input v-model.trim="row.lottable04"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px"
              :label="lottable05.value"
              v-if="lottable05.showInput == true && lottable05.showFlag == false"
              
            >
              <el-input v-model.trim="row.lottable05"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px"
              :label="doclineno.value"
              v-if="doclineno.showInput == true && doclineno.showFlag == false"
              
            >
              <el-input v-model.trim="row.doclineno"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="120px" 
            :label="pono.value"
             v-if="pono.showInput == true  && pono.showFlag == false"
             >
              <el-input v-model.trim="row.pono"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-dialog title="相关操作" :visible.sync="dialogTableVisible" width="50%">
        <el-button type="primary" round size="mini" @click="addBatch()">添加</el-button>
        <el-button type="primary" round size="mini" @click="saveBatch()">保存</el-button>
        <el-dropdown style="float:right">
          <el-button type="primary" round size="mini">
            拆分<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="dignetweight1()">分净重拆分</el-dropdown-item>
            <el-dropdown-item @click.native="diggrossweight1()">分毛重拆分</el-dropdown-item>
            <el-dropdown-item @click.native="digd_userdefine21()">分金额拆分</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <!-- <el-button type="primary" style="float:right" round size="mini" @click="digMoney()">拆分</el-button> -->
        <!-- <el-button type="danger" round size="mini" @click="deltetGriddata()">删除</el-button> -->
        <el-table
          :data="gridData"
          tooltip-effect="dark"
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
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete2(scope.$index, scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
          <!-- 动态改变部分 -->
          <el-table-column
            property="lottable01"
            v-if="lottable01.showInput == true && lottable01.showFlag == true"
            :label="lottable01.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item 
                  :prop="'gridData.' + scope.$index + '.lottable01'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model.trim="scope.row.lottable01" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            property="lottable02"
            v-if="lottable02.showInput == true && lottable02.showFlag == true"
            :label="lottable02.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item 
                  :prop="'gridData.' + scope.$index + '.lottable02'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model.trim="scope.row.lottable02" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            property="lottable03"
            v-if="lottable03.showInput == true && lottable03.showFlag == true"
            :label="lottable03.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item 
                  :prop="'gridData.' + scope.$index + '.lottable03'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model.trim="scope.row.lottable03" :maxlength="50"></el-input>
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
                  <el-input size="mini" v-model.trim="scope.row.lottable04" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            property="lottable05"
             v-if="lottable05.showInput == true && lottable05.showFlag == true"
            :label="lottable05.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item 
                  :prop="'gridData.' + scope.$index + '.lottable05'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model.trim="scope.row.lottable05" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
         <el-table-column
            property="cartonqty"
            v-if="cartonqty.showInput == true && cartonqty.showFlag == true"
            :label="cartonqty.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item 
                  :prop="'gridData.' + scope.$index + '.cartonqty'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model.trim="scope.row.cartonqty" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            property="netweight"
            v-if="netweight.showInput == true && netweight.showFlag == true"
            :label="netweight.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item 
                  :prop="'gridData.' + scope.$index + '.netweight'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model.trim="scope.row.netweight" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
                    <el-table-column
            property="grossweight"
             v-if="grossweight.showInput == true && grossweight.showFlag == true"
            :label="grossweight.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item 
                  :prop="'gridData.' + scope.$index + '.grossweight'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model.trim="scope.row.grossweight" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            property="d_userdefine2"
             v-if="d_userdefine2.showInput == true && d_userdefine2.showFlag == true"
            :label="d_userdefine2.value"
          >
            <template slot-scope="scope">
              <el-form>
                <el-form-item 
                  :prop="'gridData.' + scope.$index + '.d_userdefine2'"
                  style="margin-bottom:0px !important"
                >
                  <el-input size="mini" v-model.trim="scope.row.d_userdefine2" :maxlength="50"></el-input>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column type="index"></el-table-column>
        </el-table>
      </el-dialog>
    </div>
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
            multiple
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或
              <em>点击上传</em> 
            </div>
          </el-upload>
          <!-- <input type="file" @change="tirggerFile($event)"> -->
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="dataBaseImport($event)">确 定</el-button>
      </div>
    </el-dialog> 

            <!-- 这是编辑的dialogvislable -->
    <el-dialog title="修改內容" :visible.sync="dialogFormVisibleEdit" center width="30%">
      <el-form :model="formYwEdit" size="mini">
        <el-form-item  :label="formYwEdit.label" :label-width="formLabelWidth">
          <el-input v-model="formYwEdit.content" auto-complete="off"></el-input>
        </el-form-item> 
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisibleEdit = false" size="mini">取 消</el-button>
        <el-button type="primary" @click="eEditYwSelect()" size="mini">确 定</el-button>
      </div>
    </el-dialog> 
  </div>
</template> 
<script>
/* eslint-disable */
import axios from 'axios'
import { custInUNsame , guangLaiArr1, guanglaiSo1 } from "../../utils/importCustRequst";
import { getsession } from "../../utils/pramAll"; 
import { constants } from "crypto";
import '../../utils/publicJs';
import merge from "webpack-merge";
export default {
  name: "exportenter",
  data() {
    return {
      list: this.$route.params.list,
      detail: {},
      gridData: [],
      detailHead: {},
      tableData: [],
      headDate: {},
      row: {},
      optionsTrade: [],
      totnetweight: 0,
      totgrossweight: 0,
      totvol: 0,
      showRow: false,
      templateJson: {},
      optionsPort:[],
      optionsCust: [],
      optionsCodeDeli: [],
      optionsCountry: [],
      optionsCodeTrans: [],
      optionsCodeCurr: [],
      optionsPacktype: [],
      formYwEdit: {
        label: "",
        content: ""
      },  
      optionsDuty: [],
      optionsCust1: [],
      newTableList: [],
      tempArr: [],
      dialogTableVisible: false,
      verifyStart: "待修改",
      disabledBtn2: false,
      dnNumber: [],
      dialogFormVisible: false,
      dialogFormVisibleEdit: false,
      session: "",
      o: {},
      cartonqty:"",
      netweight:"",
      grossweight:"",
      btnAdd:"",
      totalnw:{},
      d_userdefine2:{},
      sumTot:"",
      shipperid:{},
      placeofdischarge:{},
      countryoforigin1: {},
      multipleSelection:[],
      vehicledate: {},
      shippername: {},
      vessel:{},
      asnreference1: {},
      countryoforigin: {},
      spotservice051:{},
      containertype:{},
      asnreference2:{},
      spotservice04: {},
      loading: false,
      loading2: false,
      disabledBtn: false,
      showTip: false,
      optionsCutmode: [],
      titMessage: "",
      optionsinPort:[],
      lottable01: {},
      lottable02: {},
      lottable03: {},
      lottable04: {},
      lottable05: {},
      lottable06: {},
      lottable07: {},
      lottable08: {},
      lottable09: {},
      lottable10: {},
      lottable11: {},
      lottable12: {},
      addJsonObj: {},
      doclineno: {},
      billoflading: {},
      allweigthTot : 0,
      allnegihtTot : 0,
      hscode:{},
      detailHeadRex:{},
      chinesename:{},
      pono: {},
      declarationunit:{},
      file: {},
      form: {
        name: "",
        file: []
      },
      formLabelWidth: "120px",
          rules: {
          placeofloading:[{ required: true, message: '值不能为空', trigger: 'blur' }],
          containerqty:[{ required: true, message: '值不能为空', trigger: 'blur' }], 
          countryofdestination:[{ required: true, message: '值不能为空', trigger: 'blur'}],
          deliveryterms:[{ required: true, message: '值不能为空', trigger: 'blur' }],
          placeofdelivery:[ { required: true, message: '值不能为空', trigger: 'blur' }],
          // laceofloading:[{ required: true, message: '值不能为空', trigger: 'blur' }],
          shipperid:[{ required: true, message: '值不能为空', trigger: 'blur' }],
          spotservice01:[{ required: true, message: '值不能为空', trigger: 'blur' }], 
          templateName:[{ required: true, message: '值不能为空', trigger: 'blur' }],
          totalnw:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
          spotservice02:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
           spotservice04:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
          billoflading:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
          transitmode:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
          spotservice03:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
          spotservice01: [
            { required: true, message: '值不能为空', trigger: 'blur' }
          ],
          placeofdischarge:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
           shippername: [
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
            vessel:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
            asnreference1: [
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
            countryoforigin1: [
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
            spotservice051:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
            containertype:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
            asnreference2:[
            { required: true, message: '值不能为空', trigger: 'blur' },
          ],
            vehicledate: [
            { required: true, message: '值不能为空', trigger: 'blur' }
          ]
        }
    };
  },
  mounted: function() {
    this.detail = this.$route.query;
    this.detailHead.asngroup = this.detail.customNo;
    this.detailHead.wlYwno = this.detail.ywNo;
    this.detailHead.storeid = this.detail.remark1;
    if (this.detail.status !== "未录入") {
      this.searchDetail();
    } else {
      this.getDiffentCust(this.detail);
    }
    this.allTotnum();
    this.searchparm();
     this.session = getsession(this.$router);
    // this.showRightTip();
    // this.getDiffentCust(this.detail);
  },
  beforeMount: function() {},
  // beforeRouteEnter(to, from, next) {
      // axios.post(`${process.env.VUE_APP_BASE_API}dict/getKweDictList`,{customKey: to.query.remark1})
      // .then(function(resp) {
      //   to.params.list = resp.data
      //   next()
      // })
  // },

  methods: {
   
colorChange (val) {
      
      var valFlag = 1;
      // if(this.detail.remark1 == "C000002"){
      //     const reg = /^\d{8}$/; 
      //       if (reg.test(val*1)) {
      //         return false
      //       } else {
      //         return true
      //       }
      // } else {
      //   return false;
      // }
      // console.log(this.list)
      // for(let i=0; i<this.list.length; i++) {
      //        if(this.list[i].sku == val) {
      //         valFlag =  1;
      //       } else {
      //         valFlag = 0
      //       }
      //    }

      // console.log(valFlag)
      // return valFlag;
    },
    searchDetail() {
      this.loading = true;
      const tthis = this;
      this.$http
        .get(
          `${process.env.VUE_APP_BASE_API}kwe/index/getDetailByNo?wlywno=${
            this.detail.ywNo
          }&type=${this.detail.type}`
        )
        .then(function(resp) {
          console.log("获取回来的数据");
          console.log(resp);
          tthis.detailHead = resp.data.head[0];
          tthis.tableData = resp.data.detail;
          tthis.getDiffentCust(tthis.detail);
          tthis.loading = false;
          tthis.showRow = false;
          
          if (tthis.tableData.length > 0) {
            tthis.tableData.forEach(res => {
              if (tthis.detailHead.status == 2) {
                //已审核
                tthis.disabledBtn = true;
                tthis.showTip = true;
                tthis.disabledBtn2 = false;
                tthis.titMessage = "已审核通过，如需修改请先驳回";
              }
              if (tthis.detailHead.status == 3) {
                // "已同步"
                tthis.disabledBtn = false;
                tthis.showTip = true;
                tthis.disabledBtn2 = false;
                tthis.titMessage = "该票单证已同步";
              }
              if (tthis.detailHead.status == 4) {
                //"已完成"
                tthis.disabledBtn = true;
                tthis.showTip = true;
                tthis.disabledBtn2 = false;
                tthis.titMessage = "该票单证是已完成状态";
              }
              if (tthis.detailHead.status == 6) {
                //"待修改"
                tthis.disabledBtn = false;
                tthis.showTip = true;
                tthis.disabledBtn2 = false;
                tthis.titMessage = "该票单证待修改";
              }
              if (tthis.detailHead.status == 5) {
                // "已修改"
                tthis.disabledBtn = false;
                tthis.showTip = true;
                tthis.disabledBtn2 = false;
                tthis.titMessage = "该票单证已修改";
              }
              if (tthis.detailHead.status == 1) {
                // "待审核"
                tthis.disabledBtn = false;
                tthis.showTip = true;
                tthis.disabledBtn2 = true;
                tthis.titMessage = "该票单证是待审核状态";
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
        }
    },
   getDiffentCust(detail) {
     console.log("这是外面2222")
     console.log(detail)
     console.log(this.detailHead);
    const tthis = this;
    this.$http
      .post(`${process.env.VUE_APP_BASE_API}kwe/index/getTemplateByModelId`,{modelId: detail.remark2})
      .then(function(resp) {
        tthis.templateJson = JSON.parse(resp.data.template.text);
        console.log(tthis.detail.status);
        tthis.headDate = tthis.templateJson.detailHead;
        tthis.modelHead(tthis.headDate);
        tthis.rules.placeofloading[0].required = tthis.templateJson.detailHead.placeofloading.required;
        tthis.rules.containerqty[0].required = tthis.templateJson.detailHead.containerqty.required;
        tthis.rules.countryofdestination[0].required = tthis.templateJson.detailHead.countryofdestination.required;
        tthis.rules.deliveryterms[0].required = tthis.templateJson.detailHead.deliveryterms.required;
        tthis.rules.placeofdelivery[0].required = tthis.templateJson.detailHead.placeofdelivery.required;
        tthis.rules.shipperid[0].required = tthis.templateJson.detailHead.shipperid.required;
        tthis.rules.spotservice01[0].required = tthis.templateJson.detailHead.spotservice01.required;
        tthis.rules.templateName[0].required = tthis.templateJson.detailHead.templateName.required;
        tthis.rules.totalnw[0].required = tthis.templateJson.detailHead.totalnw.required;
        tthis.rules.spotservice02[0].required = tthis.templateJson.detailHead.spotservice02.required;
        tthis.rules.spotservice04[0].required = tthis.templateJson.detailHead.spotservice04.required;
        tthis.rules.billoflading[0].required = tthis.templateJson.detailHead.billoflading.required;
        tthis.rules.transitmode[0].required = tthis.templateJson.detailHead.transitmode.required;
        tthis.rules.spotservice03[0].required = tthis.templateJson.detailHead.spotservice03.required;
        tthis.rules.placeofdischarge[0].required = tthis.templateJson.detailHead.placeofdischarge.required;
        tthis.rules.vessel[0].required = tthis.templateJson.detailHead.vessel.required;
        tthis.rules.countryoforigin1[0].required = tthis.templateJson.detailHead.countryoforigin.required;
        tthis.rules.spotservice051[0].required = tthis.templateJson.detailHead.spotservice05.required;
        tthis.rules.containertype[0].required = tthis.templateJson.detailHead.containertype.required;
        tthis.rules.asnreference2[0].required = tthis.templateJson.detailHead.asnreference2.required;
        tthis.rules.vehicledate[0].required = tthis.templateJson.detailHead.vehicledate.required;
        tthis.rules.shippername[0].required = tthis.templateJson.detailHead.shippername.required;
        tthis.rules.asnreference1[0].required = tthis.templateJson.detailHead.asnreference1.required;
        tthis.btnAdd = tthis.templateJson.detailDeploy.btnAdd
        tthis.totalnw = {
          value: tthis.templateJson.detailHead.totalnw.value,
          showFlag: tthis.templateJson.detailHead.totalnw.showFlag,
          showInput: tthis.templateJson.detailHead.totalnw.showInput
        }
         tthis.placeofdischarge = {
          value: tthis.templateJson.detailHead.placeofdischarge.value,
          showFlag: tthis.templateJson.detailHead.placeofdischarge.showFlag,
          showInput: tthis.templateJson.detailHead.placeofdischarge.showInput
        }
        tthis.shippername = {
          value: tthis.templateJson.detailHead.shippername.value,
          showFlag: tthis.templateJson.detailHead.shippername.showFlag,
          showInput: tthis.templateJson.detailHead.shippername.showInput
        }
           tthis.vessel = {
          value: tthis.templateJson.detailHead.vessel.value,
          showFlag: tthis.templateJson.detailHead.vessel.showFlag,
          showInput: tthis.templateJson.detailHead.vessel.showInput
        }
        tthis.asnreference1 = {
          value: tthis.templateJson.detailHead.asnreference1.value,
          showFlag: tthis.templateJson.detailHead.asnreference1.showFlag,
          showInput: tthis.templateJson.detailHead.asnreference1.showInput
        }
            tthis.countryoforigin1 = {
          value: tthis.templateJson.detailHead.countryoforigin.value,
          showFlag: tthis.templateJson.detailHead.countryoforigin.showFlag,
          showInput: tthis.templateJson.detailHead.countryoforigin.showInput
        }
            tthis.spotservice051 = {
          value: tthis.templateJson.detailHead.spotservice05.value,
          showFlag: tthis.templateJson.detailHead.spotservice05.showFlag,
          showInput: tthis.templateJson.detailHead.spotservice05.showInput
        }
            tthis.containertype = {
          value: tthis.templateJson.detailHead.containertype.value,
          showFlag: tthis.templateJson.detailHead.containertype.showFlag,
          showInput: tthis.templateJson.detailHead.containertype.showInput
        }
            tthis.asnreference2 = {
          value: tthis.templateJson.detailHead.asnreference2.value,
          showFlag: tthis.templateJson.detailHead.asnreference2.showFlag,
          showInput: tthis.templateJson.detailHead.asnreference2.showInput
        }
            tthis.vehicledate = {
          value: tthis.templateJson.detailHead.vehicledate.value,
          showFlag: tthis.templateJson.detailHead.vehicledate.showFlag,
          showInput: tthis.templateJson.detailHead.vehicledate.showInput
        }
        tthis.shipperid = {
          value: tthis.templateJson.detailHead.shipperid.value,
          showFlag: tthis.templateJson.detailHead.shipperid.showFlag,
          showInput: tthis.templateJson.detailHead.shipperid.showInput
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
        }

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
        tthis.doclineno = {
          value: tthis.templateJson.detailDeploy.doclineno.value,
          showFlag: tthis.templateJson.detailDeploy.doclineno.showFlag,
          showInput: tthis.templateJson.detailDeploy.doclineno.showInput
        };
        tthis.pono = {
          value: tthis.templateJson.detailDeploy.pono.value,
          showFlag: tthis.templateJson.detailDeploy.pono.showFlag,
          showInput: tthis.templateJson.detailDeploy.pono.showInput
        };
        tthis.cartonqty = {
          value: tthis.templateJson.detailDeploy.cartonqty.value,
          showFlag: tthis.templateJson.detailDeploy.cartonqty.showFlag,
          showInput: tthis.templateJson.detailDeploy.cartonqty.showInput
        };
        tthis.netweight = {
          value: tthis.templateJson.detailDeploy.netweight.value,
          showFlag: tthis.templateJson.detailDeploy.netweight.showFlag,
          showInput: tthis.templateJson.detailDeploy.netweight.showInput
        };
        tthis.grossweight = {
          value: tthis.templateJson.detailDeploy.grossweight.value,
          showFlag: tthis.templateJson.detailDeploy.grossweight.showFlag,
          showInput: tthis.templateJson.detailDeploy.grossweight.showInput
        };
        tthis.d_userdefine2 = {
          value: tthis.templateJson.detailDeploy.d_userdefine2.value,
          showFlag: tthis.templateJson.detailDeploy.d_userdefine2.showFlag,
          showInput: tthis.templateJson.detailDeploy.d_userdefine2.showInput
        };
        tthis.declarationunit = {
          value: tthis.templateJson.detailDeploy.declarationunit.value,
          showFlag: tthis.templateJson.detailDeploy.declarationunit.showFlag,
          showInput: tthis.templateJson.detailDeploy.declarationunit.showInput
        };
        tthis.hscode = {
          value: tthis.templateJson.detailDeploy.hscode.value,
          showFlag: tthis.templateJson.detailDeploy.hscode.showFlag,
          showInput: tthis.templateJson.detailDeploy.hscode.showInput
        };
        tthis.chinesename = {
          value: tthis.templateJson.detailDeploy.chinesename.value,
          showFlag: tthis.templateJson.detailDeploy.chinesename.showFlag,
          showInput: tthis.templateJson.detailDeploy.chinesename.showInput
        };
        tthis.countryoforigin = {
          value: tthis.templateJson.detailDeploy.countryoforigin.value,
          showFlag: tthis.templateJson.detailDeploy.countryoforigin.showFlag,
          showInput: tthis.templateJson.detailDeploy.countryoforigin.showInput
        }
      })
      .catch(function(error) {
        console.log(error);
      });

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
          tthis.optionsCountry = resp.data;
        });
       this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=24`)
        .then(function(resp) {
          tthis.optionsinPort = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=5`)
        .then(function(resp) {
          tthis.optionsCust1 = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=16`)
        .then(function(resp) {
          tthis.optionsTrade = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=17`)
        .then(function(resp) {
          tthis.optionsDuty = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=8`)
        .then(function(resp) {
          tthis.optionsCodeTrans = resp.data;
        });
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=11`)
        .then(function(resp) {
          tthis.optionsCutmode = resp.data;
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
            .get(
              `${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=39`
            )
            .then(function(resp) {
              tthis.optionsCust = resp.data;
            });
        }, 200);
      }
    },
    getDetails(row) {
      console.log(row);
      this.row = row;
      this.showRow = true;
      this.$refs.TableToggle.clearSelection()
      this.$refs.TableToggle.toggleRowSelection(row);
    },
    
  tableRowClassName ({row, rowIndex}) {
        //把每一行的索引放进row
       row.index = rowIndex;
    },
    copylist() {
      this.$refs.TableToggle.store.states.selection;
      if(this.multipleSelection.length != 1) {
               this.$message({
                      message: "请选择一条数组操作",
                      type: "error"
                    }) 
      } else {
        const lineData = JSON.parse(JSON.stringify(this.multipleSelection[0]));
        lineData.pid = "";
          this.tableData.push(
           lineData
          )
      }
    },
    exportExcel() {
      const _this = this;
           this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/download?ywNo=${this.detail.ywNo}`)
        .then(function(resp) {
          console.log(resp.headers)
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
    handleDelete(index, row) {
      this.tableData.splice(index, 1);
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    allTotnum() {
      console.log(this.tableData);
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
     openconfirm(message) {
        this.$alert(message, '提示', {
          dangerouslyUseHTMLString: true
        });
     },
    /* eslint-disable */
    egis() {
      // this.save();
      console.log(this.templateJson.detailDeploy);
      // 表头验证的部分
      if(this.detailHead.transitmode == "5" || this.detailHead.transitmode == "2") {
        if(!this.detailHead.billoflading) {
           this.openconfirm("运单号不能为空")
           return;
        }
      }
      if(this.detailHead.billoflading) {
        if(this.detailHead.transitmode !== "5" && this.detailHead.transitmode !== "2") {
          this.openconfirm("运输方式不合法");
          return;
        }
      }
      if(this.detailHead.transitmode == "2") {
        if(!this.detailHead.vessel) {
          this.openconfirm("当为水路运输的时候船名/航次必填");
          return;

        }
      } else {
        if(this.detailHead.vessel) {
            this.openconfirm("船名/航次必须为空");
            return;
        }
      }
      if(this.detailHead.spotservice03 == "2233" ||  this.detailHead.spotservice03 == "2244" ) {
        if(this.detailHead.transitmode != "5") {
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
        if(this.detailHead.transitmode != "2") {
          this.openconfirm("运输方式必须为水路运输");
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
      // if(this.detailHead.countryofdestination) {
      //   if(this.detailHead.countryofdestination !== "CHN") { 
      //     this.openconfirm("目的国必须是中国（142)");
      //     return;
      //   }
      // }
      if(this.detailHead.spotservice03) {
        if(this.detailHead.spotservice03 == "2225" || this.detailHead.spotservice03 == "2248") {
          if(this.detailHead.transitmode !== "2") {
            this.openconfirm("运输方式不合法");
            return;
          }
          if(this.detailHead.spotservice02 !== "3") {
            this.openconfirm("征免方式不合法");
            return;
          }
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
                            console.log(this.detailHead.asnreference1, 443453423)
                            if(!this.detailHead.asnreference1) {
                              this.openconfirm("当监管方式为一般贸易【0110】 时合同号必填");
                              return;
                            }
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
                if(this.hscode.showInput == true) {
                  if(!this.tableData[i].hscode) {
                    this.openconfirm(`第${i+1}条数据的hscode不能为空`);
                    return
                  } else {
                      if(this.tableData[i].hscode.length != 10 || isNaN(Number(this.tableData[i].hscode))) {
                      this.openconfirm(`第${i+1}条数据的hscode必须是10位数字`);
                      return;
                  }
                  }
                }
                  if(this.tableData[i].totalnetweight*1 > this.tableData[i].totalgrossweight*1) {
                    this.openconfirm(`第${i+1}条净重大于毛重`);
                    return;
                  }
                if(this.detail.remark1 == "C000046") {
                  console.log(this.tableData[i].sku.substr(0, 3));
                    if(this.tableData[i].sku.substr(0, 3) !== "3HA" && this.tableData[i].sku.substr(0, 3) !== "SHG" && this.tableData[i].sku.substr(0, 3) !== "CSG" && this.tableData[i].sku.substr(0, 3) !== "CSF") {
                      this.openconfirm(`第${i+1}条数据的料号必须以（3HA，SHG，CSG, CSF）开头`);
                      return;
                    }
                }

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
                if(this.detail.remark1 == "C000051" || this.detail.remark1 == "C000064") {
                  console.log(this.detailHead.billoflading.substr(0, 6));
                    if(this.detailHead.billoflading.substr(0, 6) == "HPHSHA" && this.detailHead.spotservice03 !== "2202") {
                        this.openconfirm(`进出口岸必须为【2202】吴淞海关`);
                        return
                    }
                }
           if(this.tableData.length>0) {
             var passNetweight = 0;
            var passGrossweight = 0;
             if(this.btnAdd == true) { 
                for(let i=0; i<this.tableData.length; i++) {
                var CARTONQTY = 0; // batch数量 
                var D_USERDEFINE2 = 0 // 分金额
                var NETWEIGHT = 0 //分净重
                var GROSSWEIGHT = 0 //分毛重
                var currList = [];
                var countryoforiginList = [];
                 if(!this.tableData[i].price) { 
                   this.openconfirm("请先计算单价");
                   return;
                 }
                  if((this.tableData[i].price.toFixed(3)*1 - (this.tableData[i].totalprice*1 / this.tableData[i].expectedqty*1).toFixed(3)*1).toFixed(3)*1 > 0.001 ||  ((this.tableData[i].totalprice*1 / this.tableData[i].expectedqty*1).toFixed(3)*1).toFixed(3)*1 -this.tableData[i].price.toFixed(3)*1  > 0.001) {
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
                   this.openconfirm(`第${i+1}条料号不合法(规则：8位数字)`);
                   return;
                 }
                 passNetweight += this.tableData[i].totalgrossweight*1;
                 passGrossweight += this.tableData[i].totalnetweight*1
                if(this.tableData[i].remark1 && this.tableData[i].remark1.length > 0) {
                  for(let k= 0; k<  this.tableData[i].remark1.length ; k++) {
                    if(JSON.stringify(this.tableData[i].remark1[k]) == '{}') {
                      this.openconfirm(`请录入第${i+1}条数据的弟${k}条数据`);
                      return;
                    } 
                  if(this.cartonqty.showInput == true) {
                     CARTONQTY += this.tableData[i].remark1[k].cartonqty*1;
                     D_USERDEFINE2 += this.tableData[i].remark1[k].d_userdefine2*1
                  }
                  if(this.netweight.showInput == true) {
                    NETWEIGHT += this.tableData[i].remark1[k].netweight*1;
                    GROSSWEIGHT += this.tableData[i].remark1[k].grossweight*1;
                    console.log(NETWEIGHT,GROSSWEIGHT)
                    if(NETWEIGHT  > GROSSWEIGHT) {
                      this.openconfirm(`第${i+1}条的数据净重大于毛重请修改`)
                    }
                  }
                  if(this.detail.remark1 == "C000047" || this.detail.remark1 == "C000065" ) {
                    console.log(this.tableData[i].remark1[k].lottable01)
                    if(this.tableData[i].remark1[k].lottable01.length != 5 || isNaN(Number(this.tableData[i].remark1[k].lottable01))) {
                      this.openconfirm(`第${i+1}条的数据里面的第${k+1}行箱号必须为五位数字`);
                      return;
                    }
                  }
                  }
                }else {
                  this.openconfirm(`请录入第${i+1}条数据的batch`);
                  return false;
                }
                if(this.cartonqty.showInput == true ) {
                  if( this.tableData[i].expectedqty*1 !== CARTONQTY*1 && this.detail.remark1 !== "C000002") {
                    this.openconfirm(`请检查第${i+1}条数据的batch数量`)
                    return false
                  }
                   }
               if(this.d_userdefine2.showInput == true) {
                    console.log((this.tableData[i].totalprice*1).toFixed(5) , (D_USERDEFINE2*1).toFixed(5))
                  if( (this.tableData[i].totalprice*1).toFixed(5) !== (D_USERDEFINE2*1).toFixed(5)) {
                    this.openconfirm(`请检查第${i+1}条数据的分金额`)
                    return false
                  }
                }
                if(this.netweight.showInput == true) {
                  if( (this.tableData[i].totalnetweight*1).toFixed(2) !== (NETWEIGHT*1).toFixed(2)) {
                    this.openconfirm(`请检查第${i+1}条数据的分净重`)
                    return false
                  }
                }

               if(this.grossweight.showInput == true) {
                  if( (this.tableData[i].totalgrossweight*1).toFixed(2) !== (GROSSWEIGHT*1).toFixed(2)) {
                    this.openconfirm(`请检查第${i+1}条数据的分毛重`)
                    return false
                  }

                if(this.tableData[i].totalnetweight > this.tableData[i].totalgrossweight*1) {
                  this.openconfirm(`第${i+1}条数据的净重大于毛重请修改`);
                  return false;
                }
                }
             
              }
              if(this.totalnw.showInput == true) {
                if(this.detailHead.totalnw*1 >  this.detailHead.spotservice01*1) {
                  this.openconfirm(`表头净重大于毛重请修改`);
                  return false;
                }
               
                if((passNetweight*1).toFixed(5) != this.detailHead.spotservice01*1 || (passGrossweight*1).toFixed(5) != this.detailHead.totalnw*1) {
                  this.openconfirm("表体的总毛重或者总净重与表头不对应");
                  return;
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
                      console.log(resp);
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
                      passNetweight += this.tableData[i].totalgrossweight*1;
                      passGrossweight += this.tableData[i].totalnetweight*1;
                    if(!this.tableData[i].price) {
                      this.openconfirm("请先保存计算单价");
                      return;
                    }
                console.log("对比单价");
                console.log((this.tableData[i].price.toFixed(3)*1 -  (this.tableData[i].totalprice*1 / this.tableData[i].expectedqty*1).toFixed(3)*1).toFixed(1)*1)
                  if((this.tableData[i].price.toFixed(3)*1 - (this.tableData[i].totalprice*1 / this.tableData[i].expectedqty*1).toFixed(3)*1).toFixed(3)*1 > 0.001 ||  ((this.tableData[i].totalprice*1 / this.tableData[i].expectedqty*1).toFixed(3)*1).toFixed(3)*1 -this.tableData[i].price.toFixed(3)*1  > 0.001) {
                    this.openconfirm(`第${i+1}条数据的单价与总价和数量的比值误差大于正负0.001`);
                    return;
                  }

                  }
                   console.log((passNetweight*1).toFixed(5) , this.detailHead.spotservice01*1 , (passGrossweight*1).toFixed(5) , this.detailHead.totalnw*1)
              if((passNetweight*1).toFixed(5) != this.detailHead.spotservice01*1 || (passGrossweight*1).toFixed(5) != this.detailHead.totalnw*1) {
                this.openconfirm("表体的总毛重或者总净重与表头不对应");
                return;
              }
                 const tthis = this;
                  this.$http
                    .post(
                      `${process.env.VUE_APP_BASE_API}kwe/index/audit?wlywno=${
                        this.detail.ywNo
                      }&status=1&op_user=${this.session}`
                    )
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
      const tthis = this;
      console.log(this.detailHead.status);
      if (this.detailHead.status == 2) {
        this.verifyStart = "待审核";
      }
      if (this.detailHead.status == 3) {
        this.verifyStart = "待修改";
      }
      this.$http
        .post(
          `${process.env.VUE_APP_BASE_API}kwe/index/audit?wlywno=${
            this.detail.ywNo
          }&status=0&op_user=${this.session}`
        )
        .then(resp => {
          if (resp.data == "updateNice") {
            tthis.$message({
              message: "更改状态成功",
              type: "success"
            });
            // tthis.disabledBtn = false;
            // tthis.showTip = false;
            // tthis.titMessage = ""
            tthis.searchDetail();
          } else {
            tthis.$message({
              message: "保存失败",
              type: "error"
            });
          }
        });
    },
    eEditYwSelect() {
        console.log(this.formYwEdit.value, this.formYwEdit.content);
     if (this.multipleSelection.length < 1) {
        this.$message.error("请选择一条数据进行操作！");
      } else {
          for(var i= 0; i<this.multipleSelection.length; i++) {
              this.multipleSelection[i][this.formYwEdit.value] = this.formYwEdit.content.replace(/^\s+|\s+$/g,"")
          };
          this.dialogFormVisibleEdit = false;
          this.save();
      }
      },
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
    saveDetailTable() {
      const tthis = this;
      this.dnNumber = [];
      tthis.tempArr = [];
      if (this.tableData.length > 0) {
        this.tableData.forEach(function(res) {
          if (res.messagehead) {
            console.log(res.messagehead);
          } else {
            res.storeid = "C000002";
            res.price = res.totalprice / res.expectedqty;
            tthis.dnNumber.push(res.customerlineno);
            let array = tthis.o[res["customerlineno"]] || [];
            array.push(res);
            tthis.o[res["customerlineno"]] = array;
            for (let item in tthis.o) {
              if (tthis.o[item].length > 0) {
                tthis.$http
                  .get(
                    "${process.env.VUE_APP_BASE_API}kwe/serialno/getMessageHead"
                  )
                  .then(function(res) {
                    tthis.o[item].forEach(resp => {
                      resp.messagehead = res.data;
                      tthis.tempArr.push(resp.messagehead);
                    });
                  });
              }
            }
          }
        });
      }
      tthis.$message({
        message: "保存成功",
        type: "success"
      });
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
    addBatch() {
      const objtemp = this.templateJson[this.detail.remark1];
      for (let prop in objtemp) {
        if (
          typeof objtemp[prop] == "object" &&
          objtemp[prop].showFlag == true
        ) {
          console.log(objtemp[prop]);
          this.addJsonObj[prop] = "";
        }
      }
      console.log(this.addJsonObj);
      this.gridData.push(JSON.parse(JSON.stringify(this.addJsonObj)));
    },
    saveBatch(index, row) {
      this.tableData[this.showindex].remark1 = this.gridData;
      // console.log(this.tableData);
      // for( let i = i; i< this.tableData.length ; i++) {
      //          for(let tabObj in this.tableData[i]) {
      //        for(let obj in this.gridData[0]) {
      //         if(tabObj =  obj) {
      //           this.tableData[i][obj] = null
      //         }
      //     }
      // }
      // }
 
      this.tableData[this.showindex]
      this.$message({
        message: "保存成功",
        type: "success"
      });
      // this.save();
      this.dialogTableVisible = false;
    },
    handleDelete2(index, row) {
      this.gridData.splice(index, 1);
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "合计";
          return;
        }
        // if (index === 1) {
        //   sums[index] = "";
        //   return;
        // }
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
    priceSave(price) {
            for (let i = 0; i < this.tableData.length; i++) {
              if(this.tableData[i].price == 'NaN')  {
               this.openconfirm(`请检查第${i+1}条数据是否有误`);
               return false;
              }
             this.tableData[i].price = ((this.tableData[i].totalprice * 1) / this.tableData[i].expectedqty *1).toFixed(price);
             console.log(this.tableData[i].price);
        }
    },
    synchroTo() {
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
                          tthis.searchDetail()
                        } else {
                          tthis.$message({
                            message: resp.data.message,
                            type: "error"
                          });
                        }
                      })
    },
    save() {
      if (this.$route.query.status == "未录入") {
        let tthis = this;
        const detailHead = [];
        detailHead.push(this.detailHead); 
         const row = [];
        const allMessage = [];
        const data = {
          inputHeadList: detailHead,
          inputDetailList: this.tableData,
          op_user: this.session
        };
        console.log("all data");
        console.log(data);
        this.$http
          .post(`${process.env.VUE_APP_BASE_API}kwe/index/inputInsert`, data)
          .then(function(resp) {
            console.log(resp);
            if (resp.data == "saveNice") {
              tthis.$message({
                message: "保存成功",
                type: "success"
              });

              tthis.$router.push({
                query: merge(tthis.$route.query, { status: "待审核" })
              });
              tthis.searchDetail();
            } else {
              tthis.$message({
                showClose: true,
                duration:0,
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
        let tthis = this;
        const row = [];
        for (var i = 0; i < this.tableData.length; i++) {
          this.tableData[i].messagehead = this.detailHead.messagehead;
          this.tableData[i].sku = this.tableData[i].sku.replace(/[‑—~]/,"-");
          this.tableData[i].customerlineno = this.tableData[i].customerlineno.replace(/[‑—~]/,"-");
          // for(let tempVal in this.tableData[i]) {
          //   console.log("test");
          //   console.log(tempVal);
          //   console.log(this.tableData[i][tempVal]);
          //   this.tableData[i][tempVal] =  this.tableData[i][tempVal].replace(/(^\s*)|(\s*$)/g, "");
          // }
          // if(this.detail.remark1 == "C000037" ) {
          //   this.tableData[i].sku = this.tableData[i].sku.replace(/\s*/g,"");
          // }
          if((this.detail.remark1 == "C000047" || this.detail.remark1 == "C000065")  && this.tableData[i].totalprice) {
                this.tableData[i].totalprice = (this.tableData[i].totalprice*1).toFixed(2);
                for(let b=0; b<this.tableData[i].remark1.length; b++) {
                  if(this.tableData[i].remark1.d_userdefine2) {
                    this.tableData[i].remark1.d_userdefine2 = (this.tableData[i].remark1.d_userdefine2*1).toFixed(2)
                  }
                }
          }
          // for(let b=0; b<this.tableData[i].remark1.length; b++) {
          //   if(this.tableData[i].remark1.price && (this.detail.remark1 == "C000047")) {
          //     this.tableData[i].remark1.price = this.tableData[i].remark1.price.toFixed(2)
          //   }
          // }
          if(this.detail.remark1 == "C000047" || this.detail.remark1 == "C000065") {
            this.tableData[i].sku = this.tableData[i].sku.replace("ff","W");
          }
          if(this.detail.remark1 == "C000047") {
            this.tableData[i].sku = this.tableData[i].sku.replace("HTFP","HJFP");
          }
          if(this.detail.remark1 == "C000067" && this.tableData[i].totalprice) {
            this.tableData[i].totalprice = this.tableData[i].totalprice.toFixed(3);
          }
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
          // 田村
          if(this.detail.remark1 == "C000004") {
            if(this.tableData[i].sku.substr(this.tableData[i].sku.length-2, 2) == "AR") {
              this.tableData[i].sku = this.tableData[i].sku.substr(this.tableData[i].sku.length-2, 2);
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
            // if(i>0){
            //     for(let c = 1; c<this.tableData.length; c++) {
            //       if(this.tableData[c].customerlineno == this.tableData[c-1].customerlineno && 
            //         this.tableData[c].sku == this.tableData[c-1].sku &&
            //         this.tableData[c].currency == this.tableData[c-1].currency &&
            //         this.tableData[c].countryoforigin == this.tableData[c-1].countryoforigin &&
            //         this.tableData[c].lottable02 == this.tableData[c-1].lottable02 &&
            //         this.tableData[c].pono == this.tableData[c-1].pono
            //         ) {
            //           this.tableData[c].expectedqty = this.tableData[c].expectedqty*1 + this.tableData[c-1].expectedqty*1;
            //           this.tableData[c].totalprice = this.tableData[c].totalprice*1 + this.tableData[c-1].totalprice*1;
            //           this.tableData[c].totalnetweight = this.tableData[c].totalnetweight*1 + this.tableData[c-1].totalnetweight*1;
            //           this.tableData[c].totalgrossweight = this.tableData[c].totalgrossweight*1 + this.tableData[c-1].totalgrossweight*1;
            //           if(this.tableData[c].remark1 && this.tableData[c].remark1.length>0){
            //             this.tableData[c-1].remark1.forEach(remark1=>{
            //               console.log(remark1);
            //               this.tableData[c].remark1.push(remark1);
            //             })
            //           }
            //           this.tableData.splice(c-1, 1)
            //           // delete this.tableData[i-1];
                      
            //           console.log("相等");
            //           console.log(this.tableData[i]);
            //         }
            //     }
            // }
          }

          // 豪熙电
          if(this.detail.remark1 == "C000054") { 
              this.tableData[i].customerlineno = this.tableData[i].customerlineno.replace("H0S", "HOS")
          }
        }
        const detailHead = [];
        detailHead.push(this.detailHead);
        const data = {
          inputHeadList: detailHead,
          inputDetailList: this.tableData,
          op_user: this.session
        };
       console.log(data)
        this.$http
          .post(`${process.env.VUE_APP_BASE_API}kwe/index/inputUpdate`, data)
          .then(function(resp) {
            console.log("success");
            console.log(resp);
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
                showClose: true,
                duration:0,
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
      }
    },
    getSummariesTable(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "总计";
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
          if (
            index == 5 ||
            index == 7 ||
            index == 8 ||
            index == 10 ||
            index == 11 ||
            index == 12
          ) {
            sums[index] = " " + sums[index].toFixed(5);
          } else {
            sums[index] = "";
          }
        } else {
          sums[index] = "";
        }
      });
      this.sumTot = sums[5];
      return sums;
    },
    addnewFormlist() {
      this.tableData.push({
        doclineno: "",
        docno: "",
        serialno: "",
        scantime: "",
        customerlineno: "",
        sku: "",
        expectedqty: "",
        price: "",
        storeid: "",
        totalprice: "",
        currency: "",
        countryoforigin: "",
        totalgrossweight: "",
        totalnetweight: "",
        totalcubic: "",
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
      console.log(this.tableData);
    },

    dataBaseImport(event) {
      const tthis = this;
      const data = {
        ywNo: this.detail.ywNo,
        excel: this.file[0]
      };
      console.log(data);

      event.preventDefault();
      let formData = new FormData();
      formData.append("ywNo", this.detail.ywNo);
      formData.append("excel", this.form.file[0]);
      let config = {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      };
      console.log(formData);
      this.$http
        .post(
          `${process.env.VUE_APP_BASE_API}/kwe/index/uploadExcel`,
          formData,
          config
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
                message: error,
                type: "error"
              });
              });
    },
    successUpload(file) {
      // 上传成功在表单的某个字段里加一个值
      this.form.file.push(file.raw);
      // this.form.file.push(file.response.result[0].url);
    },
    beforeAvatarUpload(file) {
      let Xls = file.name.split(".");
      if (Xls[1] === "xls" || Xls[1] === "xlsx") {
        return file;
      } else {
        this.$message.error("上传文件只能是 xls/xlsx 格式!");
        return false;
      }
    },
    Remove(file, fileList) {
      this.form.file = [];
    },
    tirggerFile(event) {
      console.log(event.target.files);
      this.file = event.target.files;
    },
    keyCodeNext(event) {
    const inputs = document.querySelectorAll('input');
    for(let i=0; i<inputs.length; i++) {
      if(inputs[i] == event.srcElement)  {
        inputs[i+1].focus();
      }
      // event.$refs.mySelect.handleClose()
    }
    // document.getElementsByClassName('expotrEnt').click();
    },
    submit(row) {
       console.log(55555);
      console.log(row);
      if(!row.price) {
        row.price = (row.totalprice / row.expectedqty).toFixed(2);
      }
      if(!row.totalprice) { 
        row.totalprice = (row.price * row.expectedqty).toFixed(2);
      }
    },
    weightSplitting() {
      this.allweigthTot = 0;
      this.allnegihtTot = 0;
      if(this.detailHead.spotservice01 || this.detailHead.totalnw && this.tableData.length > 0) {
        console.log("重量拆分");
        console.log(this.sumTot*1);
        var haveValnet = 0;
        var haveValgros = 0;
        var haveqty = 0;
        var haveqty1 = 0;
        for(let i=0; i<this.tableData.length; i++) {
             if(this.tableData[i].totalnetweight) {
             haveValnet = haveValnet + this.tableData[i].totalnetweight*1
             haveqty = haveqty + this.tableData[i].expectedqty*1
             console.log(haveValnet, "净重" );
             console.log(this.tableData[i])
           }
           if(this.tableData[i].totalgrossweight) {
             haveValgros = haveValgros + this.tableData[i].totalgrossweight*1
             haveqty1 = haveqty1 + this.tableData[i].expectedqty*1 
             console.log(haveValgros, "毛重")
             console.log(this.tableData[i])
           }
        }
         for(let i=0; i<this.tableData.length-1; i++) {
           if(!this.tableData[i].totalnetweight) {
             this.tableData[i].totalnetweight  =  (this.tableData[i].expectedqty*1 * (this.detailHead.totalnw*1 - haveValnet) / (this.sumTot*1 - haveqty) ).toFixed(4);  //拆分净重 
           }
           if(!this.tableData[i].totalgrossweight) {
             this.tableData[i].totalgrossweight  =  (this.tableData[i].expectedqty*1 * (this.detailHead.spotservice01*1 - haveValgros)  / (this.sumTot*1 - haveqty1) ).toFixed(2)   //拆分毛重
           }
          this.allnegihtTot = this.tableData[i].totalnetweight*1 + this.allnegihtTot*1;
          this.allweigthTot = this.tableData[i].totalgrossweight*1 + this.allweigthTot*1;
         };
         if(!this.tableData[this.tableData.length-1].totalnetweight) {
           this.tableData[this.tableData.length-1].totalnetweight = ((this.detailHead.totalnw*1 - haveValnet)- (this.allnegihtTot - haveValnet)).toFixed(4);
         }
         if(!this.tableData[this.tableData.length-1].totalgrossweight) {
           this.tableData[this.tableData.length-1].totalgrossweight = ((this.detailHead.spotservice01*1 - haveValgros)  - (this.allweigthTot - haveValgros) ).toFixed(2);
         }
      }  else {
        this.$message({
            message: "请输入总净重或者总毛重",
            type: "error"
            })
      }
    },
    SplittingNet() {
          if(this.detailHead.totalnw && this.tableData.length > 0) {
         for(let i=0; i<this.tableData.length-1; i++) {
           if(!this.tableData[i].totalgrossweight && this.tableData[i].totalnetweight) {
             this.tableData[i].totalgrossweight  =  (this.tableData[i].totalnetweight*1 * this.detailHead.spotservice01*1 / this.detailHead.totalnw*1).toFixed(2)   //拆分毛重
           }
          this.allweigthTot = this.tableData[i].totalgrossweight*1 + this.allweigthTot*1;
         };
        
        //  console.log(this.tableData[this.tableData.length-1].totalnetweight = this.totalnw - allweigth);
        if(!this.tableData[this.tableData.length-1].totalgrossweight  && this.tableData[this.tableData.length-1].totalnetweight) {
            this.tableData[this.tableData.length-1].totalgrossweight = (this.detailHead.spotservice01 - this.allweigthTot).toFixed(2);
        }
      }  else {
        this.$message({
            message: "请输入总净重",
            type: "error"
            })
      }
    },
    SplittingGross() {
          if(this.detailHead.totalnw && this.tableData.length > 0) {
         for(let i=0; i<this.tableData.length-1; i++) {
          if(!this.tableData[i].totalnetweight && this.tableData[i].totalgrossweight) {
            this.tableData[i].totalnetweight  =  (this.tableData[i].totalgrossweight*1 * this.detailHead.totalnw*1 / this.detailHead.spotservice01*1).toFixed(4)
          }
          this.allweigthTot = this.tableData[i].totalnetweight*1 + this.allweigthTot*1;
         };
        
        //  console.log(this.tableData[this.tableData.length-1].totalnetweight = this.totalnw - allweigth);
        if(!this.tableData[this.tableData.length-1].totalnetweight && this.tableData[this.tableData.length-1].totalgrossweight) {
            this.tableData[this.tableData.length-1].totalnetweight = (this.detailHead.totalnw - this.allweigthTot).toFixed(4);
        }
      }  else {
        this.$message({
            message: "请输入总净重",
            type: "error"
            })
      }
    },
    dignetweight1() {
      var totBatch = 0;
      var netweight1 = 0;
      if(this.row.remark1.length > 0) {
        for(let i= 0; i<this.row.remark1.length; i++) {
          totBatch = this.row.remark1[i].cartonqty*1 + totBatch;
        }
       for(let i= 0; i<this.row.remark1.length-1; i++) {
              console.log(this.row.remark1[i])
             this.row.remark1[i].netweight  =  (this.row.totalnetweight*1 * this.row.remark1[i].cartonqty / totBatch).toFixed(4);  //拆分净重  
              netweight1 = this.row.remark1[i].netweight*1 + netweight1;
       }
       console.log(this.row.remark1[this.row.remark1.length-1].netweight);
         this.row.remark1[this.row.remark1.length-1].netweight = (this.row.totalnetweight*1  - netweight1).toFixed(4);
      }
    },
    dignetweight1out(splitOne, split2, num) {
      this.loading = true;
      for(let i=0; i<this.tableData.length; i++){
            var totBatch = 0;
            var netweight1 = 0;
        if(this.tableData[i].remark1.length > 0) {
                  for(let k= 0; k<this.tableData[i].remark1.length; k++) {
                      totBatch = this.tableData[i].remark1[k].cartonqty*1 + totBatch;
                    }
                    for(let z= 0; z<this.tableData[i].remark1.length-1; z++) {
                          this.tableData[i].remark1[z][split2]  =  (this.tableData[i][splitOne]*1 * this.tableData[i].remark1[z].cartonqty*1 / totBatch).toFixed(num);  
                            netweight1 = this.tableData[i].remark1[z][split2]*1 + (netweight1.toFixed(num))*1;
                    }
        }
       this.tableData[i].remark1[this.tableData[i].remark1.length-1][split2] = (this.tableData[i][splitOne]*1  - netweight1).toFixed(num);
      }
      this.loading = false;
    },
    diggrossweight1out(splitOne, split2) {
      this.loading = true;
      for(let i=0; i<this.tableData.length; i++){
            var totBatch = 0;
            var netweight1 = 0;
        if(this.tableData[i].remark1.length > 0) {
                  for(let k= 0; k<this.tableData[i].remark1.length; k++) {
                    if(this.tableData[i].remark1[k].hasOwnProperty("grossweight")) {
                      console.log("这是有净重的")
                    }
                      totBatch = this.tableData[i].remark1[k].cartonqty*1 + totBatch;
                    }
                    for(let z= 0; z<this.tableData[i].remark1.length-1; z++) {
                          
                          this.tableData[i].remark1[z][splitOne]  =  (this.tableData[i].remark1[z][split2]*1 * this.tableData[i].remark1[z].cartonqty / totBatch).toFixed(5);  //拆分净重  
                            netweight1 = this.tableData[i].remark1[z][splitOne]*1 + netweight1;
                    }
        }
       this.tableData[i].remark1[this.tableData[i].remark1.length-1][split2] = (this.tableData[i][split2]*1  - netweight1).toFixed(5);
      }
      this.loading = false;
    },
    diggrossweight1() {
      var totBatch = 0;
      var grossweight1 = 0;
      if(this.row.remark1.length > 0) {
        for(let i= 0; i<this.row.remark1.length; i++) {
          totBatch = this.row.remark1[i].cartonqty*1 + totBatch;
        }
       for(let i= 0; i<this.row.remark1.length-1; i++) {
             this.row.remark1[i].grossweight  =  (this.row.totalgrossweight*1 * this.row.remark1[i].cartonqty / totBatch).toFixed(2)   //拆分毛重
              grossweight1 = this.row.remark1[i].grossweight*1 + grossweight1;
       }
       console.log(this.row.remark1[this.row.remark1.length-1].netweight);
         this.row.remark1[this.row.remark1.length-1].grossweight = (this.row.totalgrossweight*1 - grossweight1).toFixed(2);
      }
    },
    voluaNetGross() { 
          for(let i=0; i<this.tableData.length; i++) {
              let netweight = 0;
              let grossweight = 0;
              for(let b=0; b<this.tableData[i].remark1.length; b++) {
                netweight += Number(this.tableData[i].remark1[b].netweight);
                grossweight += Number(this.tableData[i].remark1[b].grossweight)
                console.log(netweight,grossweight);
              };
              this.tableData[i].totalnetweight = netweight.toFixed(4);
              this.tableData[i].totalgrossweight = grossweight.toFixed(2);
          }
    },
    digd_userdefine21() {
      var totBatch = 0;
      var d_userdefine21 = 0;
      if(this.row.remark1.length > 0) {
        for(let i= 0; i<this.row.remark1.length; i++) {
          totBatch = this.row.remark1[i].cartonqty*1 + totBatch;
        }
       for(let i= 0; i<this.row.remark1.length-1; i++) {
            if(this.detail.remark1 != "C000047" || this.detail.remark1 != "C000065") {
                 this.row.remark1[i].d_userdefine2  =  (this.row.totalprice*1 * this.row.remark1[i].cartonqty / totBatch).toFixed(3)   //拆分金额
                 d_userdefine21 = this.row.remark1[i].d_userdefine2*1 + d_userdefine21;
            } else {
                  this.row.remark1[i].d_userdefine2  =  (this.row.totalprice*1 * this.row.remark1[i].cartonqty / totBatch).toFixed(2)   //拆分金额
                 d_userdefine21 = this.row.remark1[i].d_userdefine2*1 + d_userdefine21;
            }
       }
         if(this.detail.remark1 != "C000047" || this.detail.remark1 != "C000065") {
           this.row.remark1[this.row.remark1.length-1].d_userdefine2 = (this.row.totalprice*1 - d_userdefine21).toFixed(3);
          } else {
            this.row.remark1[this.row.remark1.length-1].d_userdefine2 = (this.row.totalprice*1 - d_userdefine21).toFixed(2);
          }
      }
    },
    calculatePrice() {
        this.tableData.forEach(res=>{
          if(!res.totalprice) {
            console.log(res.cartonqty, res.price)
            res.totalprice = (Number(res.cartonqty) * Number(res.price)).toFixed(3)
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
.newBtnlist {
  margin-bottom: 10px;
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


