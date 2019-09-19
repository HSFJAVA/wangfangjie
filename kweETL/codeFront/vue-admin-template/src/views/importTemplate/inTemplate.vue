<template>
  <div class="expotrEnt app-container" v-loading="loading" element-loading-text="拼命加载中" >
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
      <el-form label-width="80px" size="mini" :model="detailHead" :rules="rules" ref="detailHead">
        <!-- 第一行  -->
        <h3>配置表头部分</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label-width="140px" label="模板名称">
              <el-input v-model="detailHead.templateName" disabled="disabled"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="elRow" >
            <el-form-item label-width="140px" label="进出口岸" :required="detailHead.spotservice03.required">
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

          <el-col :span="6" class="elRow" >
            <el-form-item label-width="140px" label="入境口岸" :required="detailHead.placeofdischarge.required">
              <el-select ref="mySelect" clearable :disabled="detailHead.placeofdischarge.disabled"
                v-model="detailHead.placeofdischarge.value"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsinPort"
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
                <el-dropdown-item> <input type="checkbox"  v-model="detailHead.placeofdischarge.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox"  v-model="detailHead.placeofdischarge.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.placeofdischarge.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          

          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="启运日期" :required="detailHead.vehicledate.required">
              <el-input v-model="detailHead.vehicledate.value" :disabled="detailHead.vehicledate.disabled" >
              </el-input>
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.vehicledate.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.vehicledate.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.vehicledate.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>

          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="境外收发货人" :required="detailHead.shippername.required" >
              <el-input v-model="detailHead.shippername.value" :disabled="detailHead.shippername.disabled" >
              </el-input>
            </el-form-item>
            <el-dropdown class="elRowBtnLeft" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.shippername.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.shippername.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.shippername.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="供应商" :required="detailHead.shipperid.required" >
              <el-input v-model="detailHead.shipperid.value" :disabled="detailHead.shipperid.disabled" >
              </el-input>
            </el-form-item>
            <el-dropdown class="elRowBtnLeft" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.shipperid.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.shipperid.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.shipperid.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="运输方式" :required="detailHead.transitmode.required">
              <!-- <el-input v-model="detailHead.transitmode"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.transitmode.value"
                :disabled="detailHead.transitmode.disabled"
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
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.transitmode.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.transitmode.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.transitmode.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        <!-- 第三行 -->
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="运单号" :required="detailHead.billoflading.required">
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

          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="船名/航次" :required="detailHead.vessel.required">
              <el-input v-model="detailHead.vessel.value" :disabled="detailHead.vessel.disabled"></el-input>
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.vessel.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.vessel.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.vessel.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>

          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="合同协议号" :required="detailHead.asnreference1.required">
              <el-input v-model="detailHead.asnreference1.value" :disabled="detailHead.asnreference1.disabled"></el-input>
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.asnreference1.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.asnreference1.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.asnreference1.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>

         <el-col :span="6" class="elRow" >
            <el-form-item label-width="140px" label="贸易国别(地区)" :required="detailHead.countryoforigin.required">
              <el-select ref="mySelect" clearable :disabled="detailHead.countryoforigin.disabled"
                v-model="detailHead.countryoforigin.value"
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
                <el-dropdown-item> <input type="checkbox"  v-model="detailHead.countryoforigin.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox"  v-model="detailHead.countryoforigin.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.countryoforigin.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>

          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="监管方式" :required="detailHead.spotservice04.required">
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
            <el-form-item label-width="140px" label="征免性质" :required="detailHead.spotservice05.required">
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
              </el-select >
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
            <el-form-item label-width="140px" label="征免方式" :required="detailHead.spotservice02.required">
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
            <el-form-item label-width="140px" label="成交方式" style="width:100%" :required="detailHead.deliveryterms.required">
              <el-select ref="mySelect" clearable
                v-model="detailHead.deliveryterms.value"
                :disabled="detailHead.deliveryterms.disabled"
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
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.deliveryterms.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.deliveryterms.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.deliveryterms.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        <!-- 第四行 -->
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="启运国" :required="detailHead.placeofloading.required">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.placeofloading.value"
                :disabled="detailHead.placeofloading.disabled"
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
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.placeofloading.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.placeofloading.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.placeofloading.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="经停港" :required="detailHead.placeofdelivery.required">
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
            <el-form-item label-width="140px" label="目的国" class="elRow" :required="detailHead.countryofdestination.required">
              <!-- <el-input v-model="detailHead.countryoforigin"></el-input> -->
              <el-select ref="mySelect" clearable
                v-model="detailHead.countryofdestination.value"
                :disabled="detailHead.countryofdestination.disabled"
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
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.countryofdestination.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.countryofdestination.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.countryofdestination.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow" >
            <el-form-item label-width="140px" label="总件数" :required="detailHead.containerqty.required">
              <el-input v-model="detailHead.containerqty.value"
              :disabled="detailHead.containerqty.disabled"
              ></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailHead.containerqty.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.containerqty.required">必填 </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailHead.containerqty.showInput"> 显示</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>

          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="包装种类" style="width:100%" :required="detailHead.containertype.required">
              <el-select ref="mySelect" clearable
                v-model="detailHead.containertype.value"
                :disabled="detailHead.containertype.disabled"
                filterable
                placeholder="请选择"
                style="width:100%"
              >
                <el-option
                  v-for="item in optionsPacktype"
                  :key="item.ncode"
                  :label="item.name + '  ['+item.ncode+']'"
                  :value="item.ncode"
                ></el-option>
              </el-select ref="mySelect">
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
          <el-col :span="6" class="elRow" >
            <el-form-item label-width="140px" label="总毛重" :required="detailHead.spotservice01.required">
              <el-input v-model="detailHead.spotservice01.value"
              :disabled="detailHead.spotservice01.disabled"
              ></el-input>
            </el-form-item>
              <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
                  <el-button size="mini"  type="info" plain>
                      <i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>  
                  <el-dropdown-menu slot="dropdown" >
                      <el-dropdown-item> <input type="checkbox" v-model="detailHead.spotservice01.disabled"> 禁用  </el-dropdown-item>
                      <el-dropdown-item><input type="checkbox" v-model="detailHead.spotservice01.required"> 必填</el-dropdown-item>
                      <el-dropdown-item><input type="checkbox" v-model="detailHead.spotservice01.showInput"> 显示</el-dropdown-item>
                  </el-dropdown-menu>
                  </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow" >
            <el-form-item label-width="140px" label="总净重" :required="detailHead.totalnw.required">
              <el-input v-model="detailHead.totalnw.value"
              :disabled="detailHead.totalnw.disabled"
              ></el-input>
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
                <el-button size="mini"  type="info" plain>
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown" >
                    <el-dropdown-item> <input type="checkbox" v-model="detailHead.totalnw.disabled"> 禁用  </el-dropdown-item>
                    <el-dropdown-item><input type="checkbox" v-model="detailHead.totalnw.required"> 必填</el-dropdown-item>
                    <el-dropdown-item><input type="checkbox" v-model="detailHead.totalnw.showInput"> 显示</el-dropdown-item>
                </el-dropdown-menu>
                </el-dropdown>
          </el-col>

          <el-col :span="6" class="elRow" >
            <el-form-item label-width="140px" label="关联报关单号" :required="detailHead.asnreference2.required">
              <el-input v-model="detailHead.asnreference2.value"
              :disabled="detailHead.asnreference2.disabled"
              ></el-input>
            </el-form-item>
            <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
                <el-button size="mini"  type="info" plain>
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown" >
                    <el-dropdown-item> <input type="checkbox" v-model="detailHead.asnreference2.disabled"> 禁用  </el-dropdown-item>
                    <el-dropdown-item><input type="checkbox" v-model="detailHead.asnreference2.required"> 必填</el-dropdown-item>
                    <el-dropdown-item><input type="checkbox" v-model="detailHead.asnreference2.showInput"> 显示</el-dropdown-item>
                </el-dropdown-menu>
                </el-dropdown>
          </el-col>
        </el-row>
      </el-form>
    </div>
     

     <div class="inFOrmtemp">
       <h3>配置表体固定部分</h3>
        <el-form label-width="80px" size="mini" :model="detailDeploy" >
        <el-row :gutter="20"  >
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="发票号" :required="detailDeploy.customerlineno.required">
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
            <el-form-item label-width="140px" label="料号"  :required="detailDeploy.sku.required">
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
            <el-form-item label-width="140px" label="数量"  :required="detailDeploy.expectedqty.required">
              <el-input v-model="detailDeploy.expectedqty.value" disabled="disabled" :disabled="detailDeploy.expectedqty.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.expectedqty.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.expectedqty.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.expectedqty.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.expectedqty.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="单价"  :required="detailDeploy.price.required">
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
            <el-form-item label-width="140px" label="总价"  :required="detailDeploy.totalprice.required">
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
            <el-form-item label-width="140px" label="币制" :required="detailDeploy.currency.required">
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
            <el-form-item label-width="140px" label="原产国" :required="detailDeploy.countryoforigin.required">
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
            <el-form-item label-width="140px" label="毛重"  :required="detailDeploy.totalgrossweight.required">
              <el-input v-model="detailDeploy.totalgrossweight.value" disabled="disabled" :disabled="detailDeploy.totalgrossweight.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.totalgrossweight.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalgrossweight.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalgrossweight.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalgrossweight.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="净重"  :required="detailDeploy.totalnetweight.required">
              <el-input v-model="detailDeploy.totalnetweight.value" disabled="disabled" :disabled="detailDeploy.totalnetweight.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.totalnetweight.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalnetweight.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalnetweight.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalnetweight.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
            <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="体积"  :required="detailDeploy.totalcubic.required">
              <el-input v-model="detailDeploy.totalcubic.value" disabled="disabled" :disabled="detailDeploy.totalcubic.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.totalcubic.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalcubic.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalcubic.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.totalcubic.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
        <h3>配置表体动态改变部分</h3>
        <el-row :gutter="20"  >
          <el-col :span="6" class="elRow">
          是否有对多： <input type="checkbox" v-model="detailDeploy.btnAdd">
          </el-col>
          </el-row>
          <el-row :gutter="20"  >
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="lottable01" :required="detailDeploy.lottable01.required">
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
            <el-form-item label-width="140px" label="lottable02"  :required="detailDeploy.lottable02.required">
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
            <el-form-item label-width="140px" label="lottable03"  :required="detailDeploy.lottable03.required">
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
            <el-form-item label-width="140px" label="lottable04"  :required="detailDeploy.lottable04.required">
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
            <el-form-item label-width="140px" label="lottable05" :required="detailDeploy.lottable05.required">
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
            <el-form-item label-width="140px" label="lottable06"  :required="detailDeploy.lottable06.required">
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
            <el-form-item label-width="140px" label="lottable07"  :required="detailDeploy.lottable07.required">
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
            <el-form-item label-width="140px" label="lottable08"  :required="detailDeploy.lottable08.required">
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
            <el-form-item label-width="140px" label="doclineno" :required="detailDeploy.doclineno.required">
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
            <el-form-item label-width="140px" label="pono"  :required="detailDeploy.pono.required">
              <el-input v-model="detailDeploy.pono.value" disabled="disabled" :disabled="detailDeploy.pono.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.pono.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.pono.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.pono.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.pono.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="cartonqty"  :required="detailDeploy.cartonqty.required">
              <el-input v-model="detailDeploy.cartonqty.value" disabled="disabled" :disabled="detailDeploy.cartonqty.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.cartonqty.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.cartonqty.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.cartonqty.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.cartonqty.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="netweight"  :required="detailDeploy.netweight.required">
              <el-input v-model="detailDeploy.netweight.value" disabled="disabled" :disabled="detailDeploy.netweight.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.netweight.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.netweight.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.netweight.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.netweight.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>



          <el-row :gutter="20"  >
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="grossweight" :required="detailDeploy.grossweight.required">
              <el-input v-model="detailDeploy.grossweight.value" disabled="disabled" :disabled="detailDeploy.grossweight.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.grossweight.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.grossweight.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.grossweight.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.grossweight.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="d_userdefine2"  :required="detailDeploy.d_userdefine2.required">
              <el-input v-model="detailDeploy.d_userdefine2.value" disabled="disabled" :disabled="detailDeploy.d_userdefine2.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.d_userdefine2.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.d_userdefine2.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.d_userdefine2.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.d_userdefine2.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="declarationunit"  :required="detailDeploy.declarationunit.required">
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
          <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="hscode"  :required="detailDeploy.hscode.required">
              <el-input v-model="detailDeploy.hscode.value" disabled="disabled" :disabled="detailDeploy.hscode.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.hscode.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.hscode.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.hscode.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.hscode.showFlag"> 对多</el-dropdown-item>
            </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="6" class="elRow">
            <el-form-item label-width="140px" label="chinesename"  :required="detailDeploy.chinesename.required">
              <el-input v-model="detailDeploy.chinesename.value" disabled="disabled" :disabled="detailDeploy.chinesename.disabled"></el-input>
            </el-form-item>
             <el-dropdown class="elRowBtn" placement="bottom-end" :hide-on-click="false" trigger="click">
            <el-button size="mini"  type="info" plain>
                <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown" >
                <el-dropdown-item> <input type="checkbox" v-model="detailDeploy.chinesename.disabled"> 禁用  </el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.chinesename.required"> 必填</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.chinesename.showInput"> 显示</el-dropdown-item>
                <el-dropdown-item><input type="checkbox" v-model="detailDeploy.chinesename.showFlag"> 对多</el-dropdown-item>
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
import axios from 'axios'
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
      detailHead: this.$route.params.detailHead,
      detailDeploy:this.$route.params.detailDeploy,
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
      optionsinPort:[],
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
      optionsCutmode:[],
      titMessage: "",
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
      allweigthTot : 0,
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
    // this.showRightTip();
    // this.getTemplateByModelId();
    // this.getDiffentCust();
  },
  beforeRouteEnter(to, from, next) {
      //  const tthis = this;
      //   console.log(this.detail.id);
      axios.post(`${process.env.VUE_APP_BASE_API}kwe/index/getTemplateByModelId`,{modelId: to.query.model_id})
      .then(function(resp) {
        to.params.detailHead = JSON.parse(resp.data.template.text).detailHead;
        to.params.detailHead.templateName = to.query.remarks1
        to.params.detailDeploy = JSON.parse(resp.data.template.text).detailDeploy;
        // to.params.dialogFormVisibleNew = false; detailHead.placeofdischarge  countryoforigin  spotservice05 asnreference1
        if(to.params.detailHead.placeofdischarge == undefined) {
          to.params.detailHead.placeofdischarge = {
              value: "",
              disabled: false,
              required: true,
              showInput: true,
              showFlag: false
          }
          to.params.detailHead.vehicledate = {
              value: "",
              disabled: false,
              required: true,
              showInput: true,
              showFlag: false
          }
          to.params.detailHead.shippername = {
              value: "",
              disabled: false,
              required: true,
              showInput: true,
              showFlag: false
          }
          to.params.detailHead.shipperid = {
              value: "",
              disabled: false,
              required: false,
              showInput: false,
              showFlag: false
          }
          to.params.detailHead.asnreference1 = {
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
          }
          to.params.detailHead.spotservice05 = {
              value: "",
              disabled: false,
              required: false ,
              showInput: true,
              showFlag: false
          }
          to.params.detailHead.countryoforigin = {
              value: "",
              disabled: false,
              required: true,
              showInput: true,
              showFlag: false
          } 
          to.params.detailHead.containertype = {
              value: "",
              disabled: false,
              required: true,
              showInput: true,
              showFlag: false
          }
          to.params.detailHead.asnreference2 = {   
              value: "",
              disabled: false,
              required: true,
              showInput: false,
              showFlag: false
          }
          to.params.detailHead.vessel = {
              value: "",
              disabled: false,
              required: false,
              showInput: true,
              showFlag: false
          }
        }
        next()
      })
      .catch(function(error) {
        console.log(error);
      });
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
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=11`)
        .then(function(resp) {
          tthis.optionsCutmode = resp.data;
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
        console.log(44444);
        console.log(JSON.parse(resp.data.template.text));
        tthis.detailHead = JSON.parse(resp.data.template.text).detailHead;
        tthis.detailDeploy = JSON.parse(resp.data.template.text).detailDeploy;
        tthis.dialogFormVisibleNew = false;
        tthis.detailHead.templateName = tthis.detail.remarks1
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
.elRowBtnLeft{
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


