<template>

<div class="app-container">
  <div>
    <el-button type="primary" size="mini" plain @click="onSubmit" icon="el-icon-search">查询</el-button>
    <el-button type="primary" size="mini" plain @click="editYw()">修改</el-button>
    <el-button type="primary" size="mini" plain @click="dialogFormVisibleNew = true">新建模板 </el-button>
    <el-button type="danger" size="mini" plain @click="deleteTab()">删除</el-button>
  </div>
  <el-form :inline="true" :model="formSearch" class="demo-form-inline" style="margin: 20px 0" size="mini">
    <el-row :gutter="24">
      <el-col :span="5">
        <el-form-item label="模板名称"  >
          <el-input  placeholder="模板名称" v-model="formSearch.name" size="mini"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="5">
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
      <el-col :span="5">
        <el-form-item label="识别引擎">
          <!-- <el-input v-model="formSearch.yewuType" placeholder="单证类型" size="mini"></el-input> -->
          <el-select v-model="formSearch.remarks2" clearable placeholder="请选择" size="mini">
            <el-option
              v-for="item in optionType2"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item> 
      </el-col>
      <el-col :span="5">
        <el-form-item label="单证类型">
          <!-- <el-input v-model="formSearch.yewuType" placeholder="单证类型" size="mini"></el-input> -->
          <el-select v-model="formSearch.yewuType" clearable placeholder="请选择" size="mini">
            <el-option
              v-for="item in optionType"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item> 
      </el-col>
            <el-col :span="4">
        <el-form-item label="创建人">
          <el-input v-model="formSearch.creater" placeholder="创建人" size="mini"></el-input>
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
    <el-table-column prop="customkey" label="客户名称" width="200px"></el-table-column>
    <el-table-column prop="name" label="模板名称" width="200px"></el-table-column>
     <el-table-column prop="remarks1" label="进出类型" width="200px"></el-table-column>
    <el-table-column prop="templateid" label="模板ID" width="200px"></el-table-column>
    <el-table-column prop="remarks2" label="识别引擎" width="200px"></el-table-column>
    <el-table-column prop="user" label="创建人" width="200px"></el-table-column>
    <el-table-column prop="time" label="创建时间" width="180px">
        <template slot-scope="scope">{{moment(scope.row.time).format("YYYY-MM-DD HH:MM:SS")}}</template>
     </el-table-column>
    <!-- <el-table-column prop="creat_time" label="创建时间" width="220px"></el-table-column> -->
    <el-table-column prop="user" label="更新人" width="200px"></el-table-column>
    <!-- <el-table-column prop="update_time" label="更新时间" width="220px"></el-table-column> -->
     <el-table-column prop="update_time" label="更新" width="180px">
        <template slot-scope="scope">{{moment(scope.row.update_time).format("YYYY-MM-DD HH:MM:SS")}}</template>
     </el-table-column>
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
  <el-dialog title="创建模板" :visible.sync="dialogFormVisibleNew" width="35%">
    <el-form :model="Newyewu" label-width="140px" size="small">
      <el-form-item label="客户名称">
        <el-select
          v-model="formInline.customkey"
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
      <el-form-item label="识别引擎">
        <el-select v-model="formInline.remarks2" placeholder="单证类型" style="width:100%">
          <el-option label="SDK（三稻）" value="SDK"></el-option>
          <el-option label="SDX（excel）" value="SDX"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="模板名称">
        <el-input v-model="formInline.name" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="模板ID">
        <el-input v-model="formInline.templateid" autocomplete="off" ></el-input>
      </el-form-item>
      <el-form-item label="单证类型">
        <el-select v-model="formInline.remarks1" placeholder="单证类型" style="width:100%">
          <el-option label="进仓" value="0"></el-option>
          <el-option label="出仓" value="1"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="editcencal()">取 消</el-button>
      <el-button type="primary" @click="submitNewYewu()">确 定</el-button>
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
    session: "",
    formLabelWidth: "80px",
    formLabelWidth1: "130px",
    loading: false,
    state2: "",
    tableData3Tot:0,
    formYwEdit: {},
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
      customkey: "",
      remarks1: "",
      remarks2: "",
      name: "",
      templateid: "",
      uesr: ""
    },
    Newyewu: {
      custname: ""
    },
    formSearch: {
      name: "",
      remarks2: "",
      onlyNo: "",
      yewuNo: "",
      custName: "",
      creater: "",
      date: "",
      yewuState: "",
      yewuType: "",
      jiedanRemark: ""
    },
    searchcCndetion:{
        page:"1", rows:"20",  remarks1:"",  name:"",  user:"",  custom:""
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
        page:"1", rows:val,remarks1:this.formSearch.yewuType,  name:this.formSearch.name,  user:this.formSearch.creater,  custom:this.formSearch.custName
    }
    this.getTable();
  },
  handleCurrentChange(val) {
    console.log(`当前页: ${val}`);
    this.searchcCndetion={
        page:val, rows:"20",remarks1:this.formSearch.yewuType,  name:this.formSearch.name,  user:this.formSearch.creater,  custom:this.formSearch.custName
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
        "idList": dataArr
      };
      this.$http.post(`${process.env.VUE_APP_BASE_API}kwe/index/deleteOcrTemplate`, deInfo).then(resp => {
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
    const tthis = this;
    if (this.multipleSelection.length != 1) {
      this.$message.error("请选择一条数据进行操作！");
    } else {
      this.dialogFormVisibleEdit = true;
      this.formYwEdit = this.multipleSelection[0];
      console.log(this.formYwEdit);
      this.formYwEdit.user = this.session;
    }
  },
  eEditYw() {
    const tthis = this;
    console.log(this.formYwEdit);
    if (this.formYwEdit.remarks1 == "进仓") {
      this.formYwEdit.remarks1 = 0;
    } else {
      this.formYwEdit.remarks1 = 1;
    }
    this.$http
      .post(`${process.env.VUE_APP_BASE_API}kwe/index/updateOcrTemplate`, this.formYwEdit)
      .then(function(res) {
        console.log(res);
        if (res.data.status == "200") {
          tthis.getTable;
          tthis.$message({
            showClose: true,
            message: "修改成功",
            type: "success"
          });
          tthis.dialogFormVisibleEdit = false;
          tthis.getTable();
        } else {
          tthis.$message({
            message: "修改失败",
            type: "error"
          });
        }
      });
  },
  editcencal() {
     this.dialogFormVisibleNew = false
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
        `${process.env.VUE_APP_BASE_API}kwe/index/getOcrList`, this.searchcCndetion)
      .then(function(response) {
        console.log(response);
        tthis.tableData3 = response.data.rows;
        tthis.tableData3Tot = response.data.count;
         tthis.tableData3.forEach(res => {
          if (res.remarks1 == 0) {
            res.remarks1 = "进仓";
          } else {
            res.remarks1 = "出仓";
          }
        });
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
  },
  onSubmit() {
    this.searchcCndetion={
        page:"1", rows:"20",  remarks1:this.formSearch.yewuType,  name:this.formSearch.name,  user:this.formSearch.creater,  custom:this.formSearch.custName,remarks2: this.formSearch.remarks2
    }
    console.log(this.searchcCndetion);
    this.getTable();
  },
  submitNewYewu() {
    console.log(this.formInline);
    this.formInline.user = this.session;
    const tthis = this;
    this.$http
      .post(`${process.env.VUE_APP_BASE_API}kwe/index/insertOcrList`, this.formInline)
      .then(function(resp) {
        tthis.dialogFormVisibleNew = false;
        if(resp.data.status = "200") {
                tthis.$message({
                    message: "新建模板成功",
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

