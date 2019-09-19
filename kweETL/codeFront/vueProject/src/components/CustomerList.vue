<template>
  <div class="CustomerList">
    <el-row class="custrowbtn">
      <el-col :span="24">
        <el-button>搜索</el-button>
        <el-button type="danger">删除</el-button>

      </el-col>
    </el-row>
    <div class="custInput">
      <el-row class>
        <el-form :label-position="labelPosition" label-width="80px" :model="formLabelAlign">
          <el-col :span="6">
            <el-form-item label="名称">
              <el-input v-model="formLabelAlign.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="活动区域">
              <el-input v-model="formLabelAlign.region"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="活动形式">
              <el-input v-model="formLabelAlign.type"></el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </div>
    <el-row class="custrowtable">
      <el-table
        height="600px"
        ref="multipleTable"
        :data="tableData3"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        border
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <!-- <el-table-column label="日期" width="120">
          <template slot-scope="scope">{{ scope.row.date }}</template>
        </el-table-column> -->
        <el-table-column prop="Item No." label="Item No." width="120"></el-table-column>
        <el-table-column prop="Date code" label="Date code" show-overflow-tooltip></el-table-column>
        <el-table-column prop="Country of origin" label="Country of origin" show-overflow-tooltip></el-table-column>
        <el-table-column prop="Finished prod No." label="Finished prod No." show-overflow-tooltip></el-table-column>
        <el-table-column prop="Lot number" label="Lot number" show-overflow-tooltip></el-table-column>
        <el-table-column prop="Lot quantity" label="Lot quantity" show-overflow-tooltip></el-table-column>
      </el-table>
    </el-row>
    <el-row class="custrowpage">
      <div class="block pagination">
        <el-pagination
          @current-change="handleCurrentChange"
          :page-sizes="[1, 2, 3, 4]"
          :page-size="100"
          layout="total, sizes, prev, pager, next, jumper"
          :total="400"
        ></el-pagination>
      </div>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "CustomerList",
  data() {
    return {
      labelPosition: "right",
      formLabelAlign: {
        name: "",
        region: "",
        type: ""
      },
		tableData3:  [{
			"Item No.": "  98825293",
			"Date code": " 1830",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8232788A9",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1834",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252648A8",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": "1830/1834",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252648A9",
			"Lot quantity": "15,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1834",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252648B1",
			"Lot quantity": " 3,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1834",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254768A1",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": "1830/1834",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254768A2",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252638A4",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": "1834/1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252638A5",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252638A6",
			"Lot quantity": " 6,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252638A7",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252638A8",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252638A9",
			"Lot quantity": " 3,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252638B1",
			"Lot quantity": "15,000"
		}, {
			"Item No.": "  98825293",
			"Date code": "1828/1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252638B2",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8252638B3",
			"Lot quantity": " 3,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1834",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA825263E05",
			"Lot quantity": " 3,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1834",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "ZA825263I09",
			"Lot quantity": " 3,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1835",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA825263N14",
			"Lot quantity": " 3,000"
		}, {
			"Item No.": "  98825293",
			"Date code": "1835/1837",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254748A1",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1837",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254748A2",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1837",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254748A3",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1837",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254748A4",
			"Lot quantity": " 6,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1837",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254748A5",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1837",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254748A6",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1837",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254748A7",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1837",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA825474C03",
			"Lot quantity": " 3,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1838",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254758A7",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": "1837/1838",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254758A8",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1838",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8254758A9",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1838",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8262108A1",
			"Lot quantity": "12,000"
		}, {
			"Item No.": "  98825293",
			"Date code": " 1838",
			"Country of origin": "   PH - Philippines",
			"Finished prod No.": " MA001492958",
			"Lot number": "   ZA8262108A2",
			"Lot quantity": " 9,000"
		}]
,
      multipleSelection: []
    };
  },
  mounted () {
    this.toggleSelection()
  },
  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    andleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
.custrowbtn {
  margin: 10px 0;
}
.pagination {
  text-align: right;
  margin-top: 10px;
}
</style>
