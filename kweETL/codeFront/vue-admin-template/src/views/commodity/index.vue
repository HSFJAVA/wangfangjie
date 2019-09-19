<template>

<div class="app-container">
  <div>
    <el-button type="primary" size="mini" plain @click="onSubmit" icon="el-icon-search">查询</el-button>
    <el-button type="primary" size="mini" plain @click="editYw()">修改</el-button>
    <el-button type="primary" size="mini" plain @click="dialogFormVisibleNew = true , flagModel = true, title = '新增'">新增料</el-button>
    <el-button type="danger" size="mini" plain @click="deleteTab()">删除</el-button>
  </div>
  <el-form :inline="true" :model="formSearch" class="demo-form-inline" style="margin: 20px 0" size="mini">
    <el-row :gutter="24">
      <el-col :span="6">
        <el-form-item label="料号"  >
          <el-input  placeholder="料号" v-model="formSearch.sku" size="mini"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="客户名称">
          <el-select
            v-model="formSearch.custName"
            filterable
            placeholder="请选择"
            style="width:100%"
            size="mini"
          >
            <el-option
              v-for="item in options4"
              :key="item.ecustomid"
              :label="item.customName"
              :value="item.key"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
            <el-col :span="6">
        <el-form-item label="HScode"  >
          <el-input  placeholder="HScode" v-model="formSearch.hscode" size="mini"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <el-table
  @cell-dblclick="tableDbEdit"
    v-loading="loading"
    element-loading-text="拼命加载中"
    height="600px"
    ref="multipleTable"
    :data="tableData3"
    tooltip-effect="dark"
    style="width: 100%"
    size="small"
    @selection-change="handleSelectionChange"
    border
    :highlight-current-row="true"
  >
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column type="index" width="50" fixed></el-table-column>
    <el-table-column prop="customKey" label="客户名称" width="200px"></el-table-column>
    <el-table-column prop="hscode" label="HScode" width="200px"></el-table-column>
    <el-table-column prop="sku" label="料号" width="200px"></el-table-column>
    <el-table-column prop="chinesename" label="中文品名" width="200px"></el-table-column>
    <el-table-column prop="coo" label="原产国" width="200px">
        <!-- <template slot-scope="scope">  
                    <el-select
                      v-model="scope.row.coo"
                      multiple 
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
        </template>-->
    </el-table-column>
    <el-table-column prop="currency" label="币制" width="200px"></el-table-column>
     <el-table-column prop="unitCn" label="单位（中文）" width="200px"></el-table-column>
    <el-table-column prop="unitEn" label="单位（英文）" width="200px"></el-table-column>
    <el-table-column prop="netweight" label="净重" width="200px"></el-table-column>
    <el-table-column prop="price" label="单价" width="200px"></el-table-column>
    <el-table-column prop="createUser" label="创建人" width="180px"></el-table-column>
    <el-table-column prop="createTime" label="创建时间" width="180px"></el-table-column>
    <!-- <el-table-column prop="creat_time" label="创建时间" width="220px"></el-table-column> -->
    <el-table-column prop="updateUser" label="更新人" width="200px"></el-table-column>
    <el-table-column prop="updateTime" label="更新时间" width="200px"></el-table-column>
    <!-- <el-table-column prop="update_time" label="更新时间" width="220px"></el-table-column> -->
    <!-- <el-table-column prop="update_time" label="更新" width="180px">
        <template slot-scope="scope">{{moment(scope.row.update_time).format("YYYY-MM-DD HH:MM:SS")}}</template>
     </el-table-column> -->
  </el-table>
  <el-row class="custrowpage"> 
    <div class="block pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[20, 40, 80, 100]"
        :page-size="10"
        :current-page="currentPage4"
        layout="total, sizes, prev, pager, next, jumper"
        :total="tableData3Tot"
      ></el-pagination>
    </div>
  </el-row>

  <!-- 新建单证的dialogvisible -->
  <el-dialog :title="title" :visible.sync="dialogFormVisibleNew" width="35%">
    <el-form :model="Newyewu" label-width="140px" size="small">
      <el-form-item label="客户名称">
        <el-select
          v-model="formInline.customKey"
          filterable
          placeholder="请选择"
          style="width:100%"
        >
          <el-option
            v-for="item in options4"
            :key="item.ecustomid"
            :label="item.customName"
            :value="item.key"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="HScode">
        <el-input v-model="formInline.hscode" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="料号">
        <el-input v-model="formInline.sku" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="中文品名">
        <el-input v-model="formInline.chinesename" autocomplete="off" ></el-input>
      </el-form-item>
     <el-form-item label="原产国">
        <el-select
          v-model="formInline.coo"
          multiple 
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
      <el-form-item label="币制">
        <el-select
          v-model="formInline.currency"
          multiple 
          filterable
          placeholder="请选择"
          style="width:100%"
        >
          <el-option
            v-for="item in optionsCodeCurr"
            :key="item.ncode"
            :label="item.customsName + '  ['+item.ncode+']'"
            :value="item.ncode"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="单位（中文)" style="width: 50%; float:left">
       <el-select
          v-model="formInline.unitCn"
          placeholder="请选择"
          style="width:100%"
        >
          <el-option
            v-for="item in optionsUnitcn"
            :key="item.ncode"
            :label="item.customsName + '  ['+item.ncode+']'"
            :value="item.ncode"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="单位（英文）" style="width: 50%; float:left">
        <el-input v-model="formInline.unitEn" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="净重" style="width: 50%; float:left">
        <el-input v-model="formInline.netweight" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="单价" style="width: 50%; float:left">
        <el-input v-model="formInline.price" autocomplete="off" ></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="editcencal()">取 消</el-button>
      <el-button v-if="flagModel" type="primary" @click="submitNewYewu()">确 定</el-button>
      <el-button v-if="!flagModel" type="primary" @click="eEditYw()">修改</el-button>
    </div>
  </el-dialog>
  <!-- 这是编辑的dialogvislable -->
  <el-dialog title="修改內容" :visible.sync="dialogFormVisibleEdit" center width="30%">
    <el-form :model="formYwEdit" size="mini">
        <el-form-item label="客户名称" :label-width="formLabelWidth">
          <el-select
            v-model="formYwEdit.customkey"
            filterable
            placeholder="请选择"
            style="width:100%"
            size="mini"
          >
            <el-option
              v-for="item in options4"
              :key="item.ecustomid"
              :label="item.customName"
              :value="item.key"
            ></el-option>
          </el-select>
        </el-form-item>
      <el-form-item label="模板名称" :label-width="formLabelWidth">
        <el-input v-model="formYwEdit.name" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="单证类型" :label-width="formLabelWidth">
          <!-- <el-input v-model="formSearch.yewuType" placeholder="单证类型" size="mini"></el-input> -->
          <el-select v-model="formYwEdit.remarks1" clearable placeholder="请选择" size="mini" style="width: 100%">
            <el-option
              v-for="item in optionType"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
              <el-form-item label="模板ID" :label-width="formLabelWidth">
        <el-input v-model="formYwEdit.templateid" auto-complete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="editcencal">取 消</el-button>
      <el-button type="primary" @click="eEditYw()">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
/* eslint-disable */
import { open } from "fs";
import { getsession } from "../../utils/pramAll";
export default {
name: "importTemplate",
data() {
  return {
    multipleTable: [],
    multipleSelection: [],
    dialogFormVisibleNew: false,
    dialogFormVisibleEdit: false,
    tableData3: [],
    title: "新增",
    flagModel: true,
    session: "",
    formLabelWidth: "80px",
    formLabelWidth1: "130px",
    loading: false,
    state2: "",
    tableData3Tot:0,
    formYwEdit: {},
    optionsUnitcn: [],
    uplodForm:{
      custNo: "",
      custName: "",
      type:"",
      remark:"",
      file: ""

    },
    edittable: false,
    optionType: [
      {
        value: 0,
        label: "进仓"
      },
      {
        value: 1,
        label: "出仓"
      }
    ],
    optionType2: [
      {
        value: "SDK",
        label: "SDX(三稻)"
      },
      {
        value: "SDX",
        label: "SDX(excel)"
      }
    ],
    formInline: {
      sku: "",
      hscode: "",
      chinesename: "",
      coo:[],
      currency:[],
      customKey: "",
      createUser: "",
      netweight: "",
      price: "",
      unitCn: "",
      unitEn:""
    },
    Newyewu: {
      custname: ""
    },
    formSearch: {
      name: "",
      custName: "",
      remarks2: "",
      onlyNo: "",
      yewuNo: "",
      sku: "",
      creater: "",
      date: "",
      hscode: "",
      yewuType: "",
      jiedanRemark: ""
    },
    searchcCndetion:{
        page:"1", rows:"20"
    },
    rules:{
          customNo: [{
            required: true, message: '请输入合适内容', trigger: 'blur'
          }]
    },
    yewuState:'',
    options4: [],
    value9: [],
    list: [],
    creater:'',
    loading: false,
    optionsCountry: [],
    optionsCodeCurr: [],
    states: [],
    status1: "",
    createDate: "",
    custNo: "",
    op_user: "",
    startDate: "",
    endDate: "",
    ywNo: "",
    currentPage4: 1,
    offset: 1,
    limit: 100,
    pickerOptions2: {
      shortcuts: [
        {
          text: "最近一周",
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit("pick", [start, end]);
          }
        },
        {
          text: "最近一个月",
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit("pick", [start, end]);
          }
        },
        {
          text: "最近三个月",
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit("pick", [start, end]);
          }
        }
      ]
    },
    value6: "",
    value7: ""
  };
},
mounted: function() {
  this.getSelect();
  this.list = this.states.map(item => {
    return { value: item, label: item };
  });
      this.session = getsession(this.$router);
      this.formSearch.creater = this.session;
      this.getTable();
},
methods: {
  querySearch(queryString, cb) {
    var restaurants = this.restaurants;
    var results = queryString
      ? restaurants.filter(this.createFilter(queryString))
      : restaurants;
    // 调用 callback 返回建议列表的数据
    cb(results);
  },
  createFilter(queryString) {
    return restaurant => {
      return (
        restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) ===
        0
      );
    };
  },
  getsession() {
    if (sessionStorage.getItem("name")) {
      this.session = sessionStorage.getItem("name");
      this.formSearch.creater = this.session;
    } else {
      this.$router.push("/login");
    }
  },

    tableDbEdit(row, column, cell, event) {
          console.log(row, column, cell, event);
          if (column.label != "顺序") {
            event.target.innerHTML = "";
            let cellInput = document.createElement("input");
            cellInput.value = "";
            cellInput.setAttribute("type", "text");
            cellInput.style.width = "80%";
            cell.appendChild(cellInput);
            cellInput.onblur = function() {
              cell.removeChild(cellInput);
              event.target.innerHTML = cellInput.value;
            };
        }
    },
  editRoll(index, row) {
    console.log(index);
    console.log(row);
    this.edittable = true;
  },
  handleSelect(item) {
    console.log(item);
  },
  handleSizeChange(val) {
    console.log(`每页 ${val} 条`);
    this.searchcCndetion={
        page:"1", rows:val,  hscode:this.formSearch.hscode,  sku:this.formSearch.sku
    }
    this.getTable();
  },
  handleCurrentChange(val) {
    console.log(`当前页: ${val}`);
    this.searchcCndetion={
        page:val, rows:"20", hscode:this.formSearch.hscode,  sku:this.formSearch.sku
    }
    console.log(this.searchcCndetion);
    this.getTable();
  },
  deleteTab() {
    const tthis = this;
    if (this.multipleSelection.length < 1) {
      this.$message.error("请选择一条数据进行操作！");
    } else {
      const dataArr = [];
      for(let i=0; i<this.multipleSelection.length; i++) {
          dataArr.push(this.multipleSelection[i].id);
      }
      const deInfo = {
        "idlist": dataArr,
        "user": this.session
      };
      this.$http.post(`${process.env.VUE_APP_BASE_API}/dict/deleteSku`, deInfo).then(resp => {
        console.log(resp);
        if (resp.data.status == "200") {
          tthis.getTable();
           tthis.$message({
            showClose: true,
            message: "删除成功",
            type: "success"
          });
        } else {
          tthis.$message({
            message: "删除失败",
            type: "error"
          });
        }
      });
    }
  },
  editYw() {
    this.title = "修改"
    this.flagModel = false;
    var coolist = [];
    const tthis = this;
    if (this.multipleSelection.length != 1) {
      this.$message.error("请选择一条数据进行操作！");
    } else {
      this.dialogFormVisibleNew = true;
      console.log(typeof this.multipleSelection[0].coo);
      this.formInline = this.multipleSelection[0];
      console.log(this.formInline);
      this.formInline.updateUser = this.session;
    }
  },
  eEditYw() {
    const tthis = this;
    this.$http
      .post(`${process.env.VUE_APP_BASE_API}dict/updateSku`, this.formInline)
      .then(function(res) {
        console.log(res);
        if (res.data.status == "200") {
          tthis.getTable;
          tthis.$message({
            showClose: true,
            message: "修改成功",
            type: "success"
          });
          tthis.dialogFormVisibleNew = false;
          tthis.getTable()
          tthis.title = "新增";
          tthis.flagModel = true;
        } else {
          tthis.$message({
            message: "修改失败",
            type: "error"
          });
        }
      });
  },
  editcencal() {
     this.dialogFormVisibleNew = false;
     this.flagModel = true;
     this.title = "新增"
  },
  handleSelectionChange(val) {
    this.multipleSelection = val;
  },
  handleClose(done) {
    this.$confirm("确认关闭？")
      .then(_ => {
        done();
      })
      .catch(_ => {});
  },
  getTable() {
    const tthis = this;
    this.$http
      .post(
        `${process.env.VUE_APP_BASE_API}dict/getSkuByParm`, this.searchcCndetion)
      .then(function(response) {
        console.log(666)
        console.log(response);
        // response.data.data.forEach(res=> {
        //   console.log(res.coo);
        //   if(res.coo) {
        //       res.coo = res.coo.replace("\"", "")
        //   }
        // })
        tthis.tableData3 = response.data.data;
        tthis.tableData3Tot = response.data.total;
      })
      .catch(function(error) {
        console.log(error);
      });
  },
  getSelect() {
    const tthis = this;
    (tthis.loading = true),
      this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/searchCustom`)
        .then(function(response) {
          console.log(response);
          tthis.options4 = response.data;
          tthis.loading = false;
        })
        .catch(function(error) {
          tthis.loading = false;
          console.log(error);
        });
        this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=6`)
        .then(function(resp) {
          tthis.optionsCountry = resp.data;
        });
        this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=2`)
        .then(function(resp) {
          tthis.optionsCodeCurr = resp.data;
        });
        this.$http
        .get(`${process.env.VUE_APP_BASE_API}kwe/index/selectCode?dictType=7`)
        .then(function(resp) {
          tthis.optionsUnitcn = resp.data;
        });
  },
  onSubmit() {
    this.searchcCndetion={
        page:"1", rows:"20",  hscode:this.formSearch.hscode,  sku:this.formSearch.sku
    }
    console.log(this.searchcCndetion);
    this.getTable();
  },
  submitNewYewu() {
    console.log(this.formInline);
    this.formInline.createUser = this.session;
    this.formInline.coo.forEach(res=> {
      res.replace(/\s*/g,"");
    })
    this.formInline.currency.forEach(res=> {
      res.replace(/\s*/g,"");
    })
    const tthis = this;
    this.$http
      .post(`${process.env.VUE_APP_BASE_API}/dict/addSku`, this.formInline)
      .then(function(resp) {
        tthis.dialogFormVisibleNew = false;
        if(resp.data.status = "200") {
                tthis.$message({
                    message: "新建料成功",
                    type: "success"
                  });
            tthis.getTable();
        } else {
                tthis.$message({
                message: "创建失败",
                type: "error"
              });
        }
      })
      .catch(function(error) {
        console.log(error);
      });
  }
}
};
</script>

<style>
html,
body {
height: 100%;
}
.pagination {
text-align: right;
margin-top: 10px;
}
</style>

