<template>
  <div class="expotrEnt app-container" v-loading="loading" element-loading-text="拼命加载中" @keyup.enter="keyCodeNext">
    <div>
      <el-button type="primary" size="mini" @click="save()" :disabled="disabledBtn" plain>保存</el-button>
      <el-alert
        style="float:right; width:300px"
        v-if="showTip"
        :title="titMessage"
        type="success"
        show-icon
      ></el-alert>
    </div>
    <br>
    <div class="inFOrmtemp">
      <el-form label-width="140px" size="mini" :model="detailHead" :rules="rules" ref="detailHead">
        <!-- 第一行  -->
        <h3>配置表头部分</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="模板名称">
              <el-input v-model="detailHead.templateName" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="elRow" >
            <el-form-item label="进出口岸" :required="detailHead.spotservice03.required">
              <el-select ref="mySelect" clearable :disabled="detailHead.spotservice03.disabled"
                v-model="detailHead.spotservice03.value"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCust1"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select >
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item> <input type="checkbox"  v-model="detailHead.spotservice03.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox"  v-model="detailHead.spotservice03.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.spotservice03.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="供应商" :required="detailHead.consigneeid.required">
              <el-input v-model="detailHead.consigneeid.value" :disabled="detailHead.consigneeid.disabled" >
              </el-input>
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.consigneeid.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.consigneeid.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.consigneeid.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="境外收发货人" :required="detailHead.consigneeaddress1.required">
              <el-input v-model="detailHead.consigneeaddress1.value" :disabled="detailHead.consigneeaddress1.disabled" >
              </el-input>
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.consigneeaddress1.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.consigneeaddress1.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.consigneeaddress1.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="运输方式" :required="detailHead.transportation.required">
              <!-- <el-input v-model="detailHead.transportation"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.transportation.value"
                :disabled="detailHead.transportation.disabled"
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
              </el-select ref="mySelect">
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.transportation.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.transportation.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.transportation.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow" >
            <el-form-item label="运单号" :required="detailHead.billoflading.required">
              <el-input v-model="detailHead.billoflading.value" :disabled="detailHead.billoflading.disabled"></el-input>
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.billoflading.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.billoflading.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.billoflading.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow" >
            <el-form-item label="合同协议号" :required="detailHead.soreference2.required">
              <el-input v-model="detailHead.soreference2.value" :disabled="detailHead.soreference2.disabled"></el-input>
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.soreference2.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.soreference2.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.soreference2.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>

          <el-col :span="6" class="elRow">
            <el-form-item label="贸易国别(地区)" :required="detailHead.countryoforigin.required">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.countryoforigin.value"
                :disabled="detailHead.countryoforigin.disabled"
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
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.countryoforigin.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.countryoforigin.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.countryoforigin.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>


          <el-col :span="6" class="elRow">
            <el-form-item label="监管方式" :required="detailHead.spotservice04.required">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.spotservice04.value"
                :disabled="detailHead.spotservice04.disabled"
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
              </el-select >
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.spotservice04.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.spotservice04.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.spotservice04.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="征免性质" :required="detailHead.spotservice05.required">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.spotservice05.value"
                :disabled="detailHead.spotservice05.disabled"
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
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.spotservice05.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.spotservice05.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.spotservice05.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="征免方式" :required="detailHead.spotservice02.required">
              <!-- <el-input v-model="detailHead.spotservice02"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.spotservice02.value"
                :disabled="detailHead.spotservice02.disabled"
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
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.spotservice02.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.spotservice02.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.spotservice02.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="成交方式" style="width:100%" :required="detailHead.consigneeaddress2.required">
              <el-select ref="mySelect" clearable
                v-model="detailHead.consigneeaddress2.value"
                :disabled="detailHead.consigneeaddress2.disabled"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCodeDeli"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsName"
                ></el-option>
              </el-select ref="mySelect">
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.consigneeaddress2.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.consigneeaddress2.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.consigneeaddress2.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="运抵国" :required="detailHead.placeofdischarge.required">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.placeofdischarge.value"
                :disabled="detailHead.placeofdischarge.disabled"
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
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.placeofdischarge.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.placeofdischarge.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.placeofdischarge.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="指运港" :required="detailHead.placeofdelivery.required">
              <el-select ref="mySelect" clearable
                v-model="detailHead.placeofdelivery.value"
                :disabled="detailHead.placeofdelivery.disabled"
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
              </el-select >
            </el-form-item>
           <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.placeofdelivery.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.placeofdelivery.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.placeofdelivery.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>

          <el-col :span="6" class="elRow">
            <el-form-item label="包装种类" :required="detailHead.containertype.required">
              <el-select ref="mySelect" clearable
                v-model="detailHead.containertype.value"
                :disabled="detailHead.containertype.disabled"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsPacktype"
                  :key="item.id"
                  :label="item.customsName + '  ['+item.customsCode+']' + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select >
            </el-form-item>
           <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.containertype.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.containertype.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.containertype.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
      </el-form>
    </div>
     

     <div class="inFOrmtemp">
       <h3>配置表体固定部分</h3>
        <el-form label-width="140px" size="mini" :model="detailDeploy" >
        <el-row :gutter="20"  >
          <el-col :span="6" class="elRow">
            <el-form-item label="发票号" :required="detailDeploy.customerlineno.required">
              <el-input v-model="detailDeploy.customerlineno.value" disabled="disabled" :disabled="detailDeploy.customerlineno.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.customerlineno.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.customerlineno.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.customerlineno.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.customerlineno.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="料号"  :required="detailDeploy.sku.required">
              <el-input v-model="detailDeploy.sku.value" disabled="disabled" :disabled="detailDeploy.sku.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.sku.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.sku.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.sku.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.sku.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        <el-col :span="6" class="elRow">
            <el-form-item label="数量"  :required="detailDeploy.openqty.required">
              <el-input v-model="detailDeploy.openqty.value" disabled="disabled" :disabled="detailDeploy.openqty.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.openqty.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.openqty.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.openqty.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.openqty.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="单价"  :required="detailDeploy.price.required">
              <el-input v-model="detailDeploy.price.value" disabled="disabled" :disabled="detailDeploy.price.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.price.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.price.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.price.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.price.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
         <el-row :gutter="20"  >
            <el-col :span="6" class="elRow">
            <el-form-item label="总价"  :required="detailDeploy.totalprice.required">
              <el-input v-model="detailDeploy.totalprice.value" disabled="disabled" :disabled="detailDeploy.totalprice.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.totalprice.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalprice.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalprice.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalprice.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
           <el-col :span="6" class="elRow" >
            <el-form-item label="币制" :required="detailDeploy.currency.required">
              <el-select ref="mySelect" clearable :disabled="detailDeploy.currency.disabled"
                v-model="detailDeploy.currency.value"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCodeCurr"
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select >
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item> <input type="checkbox"  v-model="detailDeploy.currency.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox"  v-model="detailDeploy.currency.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.currency.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow" >
            <el-form-item label="原产国" :required="detailDeploy.countryoforigin.required">
              <el-select ref="mySelect" clearable :disabled="detailDeploy.countryoforigin.disabled"
                v-model="detailDeploy.countryoforigin.value"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsCountry" 
                  :key="item.customsName"
                  :label="item.customsName + '  ['+item.customsCode+']'"
                  :value="item.customsCode"
                ></el-option>
              </el-select >
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item> <input type="checkbox"  v-model="detailDeploy.countryoforigin.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox"  v-model="detailDeploy.countryoforigin.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.countryoforigin.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>

          <el-col :span="6" class="elRow">
            <el-form-item label="单位"  :required="detailDeploy.declarationunit.required">
              <el-input v-model="detailDeploy.declarationunit.value" disabled="disabled" :disabled="detailDeploy.declarationunit.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.declarationunit.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.declarationunit.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.declarationunit.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.declarationunit.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
        <h3>配置表体动态改变部分</h3>
        是否有对多： <input type="checkbox" v-model="detailDeploy.btnAdd">
          <el-row :gutter="20"  >
          <el-col :span="6" class="elRow">
            <el-form-item label="lottable01" :required="detailDeploy.lottable01.required">
              <el-input v-model="detailDeploy.lottable01.value" disabled="disabled" :disabled="detailDeploy.lottable01.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.lottable01.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable01.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable01.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable01.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="lottable02"  :required="detailDeploy.lottable02.required">
              <el-input v-model="detailDeploy.lottable02.value" disabled="disabled" :disabled="detailDeploy.lottable02.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.lottable02.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable02.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable02.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable02.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        <el-col :span="6" class="elRow">
            <el-form-item label="lottable03"  :required="detailDeploy.lottable03.required">
              <el-input v-model="detailDeploy.lottable03.value" disabled="disabled" :disabled="detailDeploy.lottable03.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.lottable03.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable03.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable03.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable03.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="lottable04"  :required="detailDeploy.lottable04.required">
              <el-input v-model="detailDeploy.lottable04.value" disabled="disabled" :disabled="detailDeploy.lottable04.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.lottable04.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable04.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable04.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable04.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>

          <el-row :gutter="20"  >
          <el-col :span="6" class="elRow">
            <el-form-item label="lottable05" :required="detailDeploy.lottable05.required">
              <el-input v-model="detailDeploy.lottable05.value" disabled="disabled" :disabled="detailDeploy.lottable05.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.lottable05.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable05.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable05.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable05.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="lottable06"  :required="detailDeploy.lottable06.required">
              <el-input v-model="detailDeploy.lottable06.value" disabled="disabled" :disabled="detailDeploy.lottable06.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.lottable06.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable06.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable06.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable06.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        <el-col :span="6" class="elRow">
            <el-form-item label="lottable07"  :required="detailDeploy.lottable07.required">
              <el-input v-model="detailDeploy.lottable07.value" disabled="disabled" :disabled="detailDeploy.lottable07.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.lottable07.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable07.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable07.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable07.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="lottable08"  :required="detailDeploy.lottable08.required">
              <el-input v-model="detailDeploy.lottable08.value" disabled="disabled" :disabled="detailDeploy.lottable04.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.lottable08.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable08.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable08.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.lottable08.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>

          <el-row :gutter="20"  >
          <el-col :span="6" class="elRow">
            <el-form-item label="doclineno" :required="detailDeploy.doclineno.required">
              <el-input v-model="detailDeploy.doclineno.value" disabled="disabled" :disabled="detailDeploy.doclineno.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.doclineno.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.doclineno.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.doclineno.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.doclineno.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="volumn"  :required="detailDeploy.volumn.required">
              <el-input v-model="detailDeploy.volumn.value" disabled="disabled" :disabled="detailDeploy.volumn.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.volumn.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.volumn.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.volumn.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.volumn.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        <el-col :span="6" class="elRow">
            <el-form-item label="qtyordered"  :required="detailDeploy.qtyordered.required">
              <el-input v-model="detailDeploy.qtyordered.value" disabled="disabled" :disabled="detailDeploy.qtyordered.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.qtyordered.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.qtyordered.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.qtyordered.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.qtyordered.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label="volumnuom"  :required="detailDeploy.volumnuom.required">
              <el-input v-model="detailDeploy.volumnuom.value" disabled="disabled" :disabled="detailDeploy.volumnuom.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click"> 
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.volumnuom.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.volumnuom.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.volumnuom.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.volumnuom.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
      </el-form>
     </div>


  </div>
</template>
<script>
/* eslint-disable */
import { custInUNsame } from "../../utils/importCustRequst";
import { getsession } from "../../utils/pramAll";
import { constants } from "crypto";
import merge from "webpack-merge";
export default {
  name: "ImportEnteringTemplate",
  data() {
    return {
      detail: {},
      gridData: [],
      detailHead: {
          spotservice03:{
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          consigneeid:{
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          transportation: {
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          billoflading: {
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
         spotservice04: {
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          spotservice02: {
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          spotservice01: {
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          placeofdischarge: {
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          placeofdelivery:{
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          consigneeaddress1:{
              value: "",
              disabled: false,
              required: true,
              showInput: true
          },
          soreference2:{
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          countryoforigin:{
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          spotservice05:{
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          consigneeaddress2:{
              value: "",
              disabled: false,
              required: false,
              showInput: true
          },
          containertype:{
              value: "",
              disabled: false,
              required: false,
              showInput: true
          }
      },
      detailDeploy:{
        customerlineno:{
              value: "",
              disabled: false,
              required: true,
              showInput: true,
              showFlag: false
        },
        sku:{
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
        },
        openqty:{
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
        },
        price:{
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
        },
        totalprice:{
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
        },
        currency:{
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
        },
        countryoforigin: {
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
        },
        lottable01: {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        lottable02: {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        lottable03: {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        lottable04: {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        lottable05: {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        lottable06: {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        lottable07: {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        lottable08: {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        doclineno: {
              value: "行号",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        qtyordered: {
              value: "batch数量",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        declarationunit: {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        volumn: {
              value: "分金额",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        volumnuom:{
              value: "入库发票号",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        },
        containernum:{
              value: "第二品名",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        }


      },
      tableData: [],
      row: {},
      // totqty: 0,
      // totprice: 0,
      optionsTrade: [],
      totnetweight: 0,
      totgrossweight: 0,
      totvol: 0,
      showRow: false,
      templateJson: {},
      optionsCust: [],
      optionsCodeDeli: [],
      optionsCountry: [],
      optionsCodeTrans: [],
      optionsCodeCurr: [],
      optionsPacktype: [],
      optionsDuty: [],
      optionsCust1: [],
      tempArr: [],
      dialogTableVisible: false,
      verifyStart: "待修改",
      disabledBtn2: false,
      dnNumber: [],
      session: "",
      o: {},
      cartonqty:"",
      netweight:"",
      grossweight:"",
      d_userdefine2:{},
      sumTot:"",
      multipleSelection:[],
      loading: false,
      loading2: false,
      disabledBtn: false,
      showTip: false,
      titMessage: "",
      consigneeaddress1: {},
      soreference2:{},
      countryoforigin:{},
      spotservice05:{},
      consigneeaddress2:{},
      containertype: {},
      lottable01: {},
      optionsCutmode:[],
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
      allweigthTot : 0,
      qtyordered:{},
      allnegihtTot : 0,
      hscode:{},
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
          number: [
            { required: true, message: '请输入活动名称', trigger: 'blur' },
            { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          ],
        }
    };
  },
  mounted: function() {
    this.detail = this.$route.query;
    console.log(this.detail);
        this.allTotnum();
    this.searchparm();
     this.session = getsession(this.$router);
     this.getTemplateByModelId()
      // this.searchDetail();

    // this.showRightTip();
    // this.getDiffentCust();
  },
  beforeMount: function() {},
  methods: {
    searchDetail() {
      this.loading = true;
      const tthis = this;
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
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=11`)
        .then(function(resp) {
          tthis.optionsCutmode = resp.data;
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
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=2`)
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
    handleDelete(index, row) {
      this.tableData.splice(index, 1);
    },
    handleSelectionChange(val) {
      console.log(val);
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

    save() {
      const tthis = this;
      const data = {
      detailHead:this.detailHead,
      detailDeploy: this.detailDeploy
      }
      console.log(data);
      this.$http
      .post(`${process.env.VUE_APP_BASE_API}kwe/index/updateTemplate`,{
        model_id:  this.detail.model_id,
        text: data,
        user: this.session
      })
      .then(function(resp) {
        console.log(resp);
        if(resp.data.status == "200") {
                tthis.$message({
                    message: "保存成功",
                    type: "success"
                  });
                  tthis.getTemplateByModelId();
        } else {
                tthis.$message({
                message: resp.data.message,
                type: "error"
              });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
    },
    getTemplateByModelId() {
    const tthis = this;
    console.log(this.detail.id);
    this.$http
      .post(`${process.env.VUE_APP_BASE_API}kwe/index/getTemplateByModelId`,{modelId: this.detail.model_id})
      .then(function(resp) {
        console.log(555555555);
        console.log(JSON.parse(resp.data.template.text).detailDeploy);
        tthis.detailHead = JSON.parse(resp.data.template.text).detailHead;
        tthis.detailDeploy = JSON.parse(resp.data.template.text).detailDeploy;
        console.log(tthis.detailDeploy.declarationunit);
        if(!tthis.detailDeploy.declarationunit) {
           tthis.detailDeploy['declarationunit'] = {
              value: "",
              disabled: false,
              required: false,
              showInput: false, 
              showFlag: false
        }
        }
        if(tthis.detailHead.consigneeaddress1 == undefined) {
           tthis.detailHead.consigneeaddress1 = {
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
        }

        tthis.detailHead['soreference2'] = {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
        }

        tthis.detailHead['countryoforigin'] = {
              value: "",
              disabled: false,
              required: true,
              showInput: true,
              showFlag: false
        }

        tthis.detailHead['spotservice05'] = {
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
        }

        tthis.detailHead['consigneeaddress2'] = {
              value: "",
              disabled: false,
              required: true,
              showInput: true,
              showFlag: false
        }
        tthis.detailHead['containertype'] = {
              value: "",
              disabled: false,
              required: true,
              showInput: true,
              showFlag: false
        }
        }
        tthis.detailHead.templateName = tthis.detail.remarks1
        tthis.dialogFormVisibleNew = false;
      })
      .catch(function(error) {
        console.log(error);
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
.elRow{
    position: relative;
}
.elRowBtn{
    position: absolute !important;
    left: 150px;
    top: 0;
    z-index: 8;
}
.inFOrmtemp  .el-input__inner{
    padding-left: 60px
}
input:required{
    background: #f56c6c;
}
.el-select:required{
    background: #f56c6c !important;
}

</style>


