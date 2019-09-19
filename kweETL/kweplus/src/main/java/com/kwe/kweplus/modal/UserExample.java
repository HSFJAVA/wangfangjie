package com.kwe.kweplus.modal;

import java.util.ArrayList;
import java.util.List;

public class UserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(String value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(String value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(String value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(String value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(String value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(String value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLike(String value) {
            addCriterion("uid like", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotLike(String value) {
            addCriterion("uid not like", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<String> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<String> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(String value1, String value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(String value1, String value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUnameIsNull() {
            addCriterion("uname is null");
            return (Criteria) this;
        }

        public Criteria andUnameIsNotNull() {
            addCriterion("uname is not null");
            return (Criteria) this;
        }

        public Criteria andUnameEqualTo(String value) {
            addCriterion("uname =", value, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameNotEqualTo(String value) {
            addCriterion("uname <>", value, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameGreaterThan(String value) {
            addCriterion("uname >", value, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameGreaterThanOrEqualTo(String value) {
            addCriterion("uname >=", value, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameLessThan(String value) {
            addCriterion("uname <", value, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameLessThanOrEqualTo(String value) {
            addCriterion("uname <=", value, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameLike(String value) {
            addCriterion("uname like", value, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameNotLike(String value) {
            addCriterion("uname not like", value, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameIn(List<String> values) {
            addCriterion("uname in", values, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameNotIn(List<String> values) {
            addCriterion("uname not in", values, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameBetween(String value1, String value2) {
            addCriterion("uname between", value1, value2, "uname");
            return (Criteria) this;
        }

        public Criteria andUnameNotBetween(String value1, String value2) {
            addCriterion("uname not between", value1, value2, "uname");
            return (Criteria) this;
        }

        public Criteria andChinesenameIsNull() {
            addCriterion("chinesename is null");
            return (Criteria) this;
        }

        public Criteria andChinesenameIsNotNull() {
            addCriterion("chinesename is not null");
            return (Criteria) this;
        }

        public Criteria andChinesenameEqualTo(String value) {
            addCriterion("chinesename =", value, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameNotEqualTo(String value) {
            addCriterion("chinesename <>", value, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameGreaterThan(String value) {
            addCriterion("chinesename >", value, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameGreaterThanOrEqualTo(String value) {
            addCriterion("chinesename >=", value, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameLessThan(String value) {
            addCriterion("chinesename <", value, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameLessThanOrEqualTo(String value) {
            addCriterion("chinesename <=", value, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameLike(String value) {
            addCriterion("chinesename like", value, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameNotLike(String value) {
            addCriterion("chinesename not like", value, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameIn(List<String> values) {
            addCriterion("chinesename in", values, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameNotIn(List<String> values) {
            addCriterion("chinesename not in", values, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameBetween(String value1, String value2) {
            addCriterion("chinesename between", value1, value2, "chinesename");
            return (Criteria) this;
        }

        public Criteria andChinesenameNotBetween(String value1, String value2) {
            addCriterion("chinesename not between", value1, value2, "chinesename");
            return (Criteria) this;
        }

        public Criteria andPwdIsNull() {
            addCriterion("pwd is null");
            return (Criteria) this;
        }

        public Criteria andPwdIsNotNull() {
            addCriterion("pwd is not null");
            return (Criteria) this;
        }

        public Criteria andPwdEqualTo(String value) {
            addCriterion("pwd =", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotEqualTo(String value) {
            addCriterion("pwd <>", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdGreaterThan(String value) {
            addCriterion("pwd >", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdGreaterThanOrEqualTo(String value) {
            addCriterion("pwd >=", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdLessThan(String value) {
            addCriterion("pwd <", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdLessThanOrEqualTo(String value) {
            addCriterion("pwd <=", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdLike(String value) {
            addCriterion("pwd like", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotLike(String value) {
            addCriterion("pwd not like", value, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdIn(List<String> values) {
            addCriterion("pwd in", values, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotIn(List<String> values) {
            addCriterion("pwd not in", values, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdBetween(String value1, String value2) {
            addCriterion("pwd between", value1, value2, "pwd");
            return (Criteria) this;
        }

        public Criteria andPwdNotBetween(String value1, String value2) {
            addCriterion("pwd not between", value1, value2, "pwd");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andRemarks1IsNull() {
            addCriterion("remarks1 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks1IsNotNull() {
            addCriterion("remarks1 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks1EqualTo(String value) {
            addCriterion("remarks1 =", value, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1NotEqualTo(String value) {
            addCriterion("remarks1 <>", value, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1GreaterThan(String value) {
            addCriterion("remarks1 >", value, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1GreaterThanOrEqualTo(String value) {
            addCriterion("remarks1 >=", value, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1LessThan(String value) {
            addCriterion("remarks1 <", value, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1LessThanOrEqualTo(String value) {
            addCriterion("remarks1 <=", value, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1Like(String value) {
            addCriterion("remarks1 like", value, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1NotLike(String value) {
            addCriterion("remarks1 not like", value, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1In(List<String> values) {
            addCriterion("remarks1 in", values, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1NotIn(List<String> values) {
            addCriterion("remarks1 not in", values, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1Between(String value1, String value2) {
            addCriterion("remarks1 between", value1, value2, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks1NotBetween(String value1, String value2) {
            addCriterion("remarks1 not between", value1, value2, "remarks1");
            return (Criteria) this;
        }

        public Criteria andRemarks2IsNull() {
            addCriterion("remarks2 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks2IsNotNull() {
            addCriterion("remarks2 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks2EqualTo(String value) {
            addCriterion("remarks2 =", value, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2NotEqualTo(String value) {
            addCriterion("remarks2 <>", value, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2GreaterThan(String value) {
            addCriterion("remarks2 >", value, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2GreaterThanOrEqualTo(String value) {
            addCriterion("remarks2 >=", value, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2LessThan(String value) {
            addCriterion("remarks2 <", value, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2LessThanOrEqualTo(String value) {
            addCriterion("remarks2 <=", value, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2Like(String value) {
            addCriterion("remarks2 like", value, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2NotLike(String value) {
            addCriterion("remarks2 not like", value, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2In(List<String> values) {
            addCriterion("remarks2 in", values, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2NotIn(List<String> values) {
            addCriterion("remarks2 not in", values, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2Between(String value1, String value2) {
            addCriterion("remarks2 between", value1, value2, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks2NotBetween(String value1, String value2) {
            addCriterion("remarks2 not between", value1, value2, "remarks2");
            return (Criteria) this;
        }

        public Criteria andRemarks3IsNull() {
            addCriterion("remarks3 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks3IsNotNull() {
            addCriterion("remarks3 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks3EqualTo(String value) {
            addCriterion("remarks3 =", value, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3NotEqualTo(String value) {
            addCriterion("remarks3 <>", value, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3GreaterThan(String value) {
            addCriterion("remarks3 >", value, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3GreaterThanOrEqualTo(String value) {
            addCriterion("remarks3 >=", value, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3LessThan(String value) {
            addCriterion("remarks3 <", value, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3LessThanOrEqualTo(String value) {
            addCriterion("remarks3 <=", value, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3Like(String value) {
            addCriterion("remarks3 like", value, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3NotLike(String value) {
            addCriterion("remarks3 not like", value, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3In(List<String> values) {
            addCriterion("remarks3 in", values, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3NotIn(List<String> values) {
            addCriterion("remarks3 not in", values, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3Between(String value1, String value2) {
            addCriterion("remarks3 between", value1, value2, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks3NotBetween(String value1, String value2) {
            addCriterion("remarks3 not between", value1, value2, "remarks3");
            return (Criteria) this;
        }

        public Criteria andRemarks4IsNull() {
            addCriterion("remarks4 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks4IsNotNull() {
            addCriterion("remarks4 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks4EqualTo(String value) {
            addCriterion("remarks4 =", value, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4NotEqualTo(String value) {
            addCriterion("remarks4 <>", value, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4GreaterThan(String value) {
            addCriterion("remarks4 >", value, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4GreaterThanOrEqualTo(String value) {
            addCriterion("remarks4 >=", value, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4LessThan(String value) {
            addCriterion("remarks4 <", value, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4LessThanOrEqualTo(String value) {
            addCriterion("remarks4 <=", value, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4Like(String value) {
            addCriterion("remarks4 like", value, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4NotLike(String value) {
            addCriterion("remarks4 not like", value, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4In(List<String> values) {
            addCriterion("remarks4 in", values, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4NotIn(List<String> values) {
            addCriterion("remarks4 not in", values, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4Between(String value1, String value2) {
            addCriterion("remarks4 between", value1, value2, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks4NotBetween(String value1, String value2) {
            addCriterion("remarks4 not between", value1, value2, "remarks4");
            return (Criteria) this;
        }

        public Criteria andRemarks5IsNull() {
            addCriterion("remarks5 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks5IsNotNull() {
            addCriterion("remarks5 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks5EqualTo(String value) {
            addCriterion("remarks5 =", value, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5NotEqualTo(String value) {
            addCriterion("remarks5 <>", value, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5GreaterThan(String value) {
            addCriterion("remarks5 >", value, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5GreaterThanOrEqualTo(String value) {
            addCriterion("remarks5 >=", value, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5LessThan(String value) {
            addCriterion("remarks5 <", value, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5LessThanOrEqualTo(String value) {
            addCriterion("remarks5 <=", value, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5Like(String value) {
            addCriterion("remarks5 like", value, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5NotLike(String value) {
            addCriterion("remarks5 not like", value, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5In(List<String> values) {
            addCriterion("remarks5 in", values, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5NotIn(List<String> values) {
            addCriterion("remarks5 not in", values, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5Between(String value1, String value2) {
            addCriterion("remarks5 between", value1, value2, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks5NotBetween(String value1, String value2) {
            addCriterion("remarks5 not between", value1, value2, "remarks5");
            return (Criteria) this;
        }

        public Criteria andRemarks6IsNull() {
            addCriterion("remarks6 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks6IsNotNull() {
            addCriterion("remarks6 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks6EqualTo(String value) {
            addCriterion("remarks6 =", value, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6NotEqualTo(String value) {
            addCriterion("remarks6 <>", value, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6GreaterThan(String value) {
            addCriterion("remarks6 >", value, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6GreaterThanOrEqualTo(String value) {
            addCriterion("remarks6 >=", value, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6LessThan(String value) {
            addCriterion("remarks6 <", value, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6LessThanOrEqualTo(String value) {
            addCriterion("remarks6 <=", value, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6Like(String value) {
            addCriterion("remarks6 like", value, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6NotLike(String value) {
            addCriterion("remarks6 not like", value, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6In(List<String> values) {
            addCriterion("remarks6 in", values, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6NotIn(List<String> values) {
            addCriterion("remarks6 not in", values, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6Between(String value1, String value2) {
            addCriterion("remarks6 between", value1, value2, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks6NotBetween(String value1, String value2) {
            addCriterion("remarks6 not between", value1, value2, "remarks6");
            return (Criteria) this;
        }

        public Criteria andRemarks7IsNull() {
            addCriterion("remarks7 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks7IsNotNull() {
            addCriterion("remarks7 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks7EqualTo(String value) {
            addCriterion("remarks7 =", value, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7NotEqualTo(String value) {
            addCriterion("remarks7 <>", value, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7GreaterThan(String value) {
            addCriterion("remarks7 >", value, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7GreaterThanOrEqualTo(String value) {
            addCriterion("remarks7 >=", value, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7LessThan(String value) {
            addCriterion("remarks7 <", value, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7LessThanOrEqualTo(String value) {
            addCriterion("remarks7 <=", value, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7Like(String value) {
            addCriterion("remarks7 like", value, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7NotLike(String value) {
            addCriterion("remarks7 not like", value, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7In(List<String> values) {
            addCriterion("remarks7 in", values, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7NotIn(List<String> values) {
            addCriterion("remarks7 not in", values, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7Between(String value1, String value2) {
            addCriterion("remarks7 between", value1, value2, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks7NotBetween(String value1, String value2) {
            addCriterion("remarks7 not between", value1, value2, "remarks7");
            return (Criteria) this;
        }

        public Criteria andRemarks8IsNull() {
            addCriterion("remarks8 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks8IsNotNull() {
            addCriterion("remarks8 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks8EqualTo(String value) {
            addCriterion("remarks8 =", value, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8NotEqualTo(String value) {
            addCriterion("remarks8 <>", value, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8GreaterThan(String value) {
            addCriterion("remarks8 >", value, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8GreaterThanOrEqualTo(String value) {
            addCriterion("remarks8 >=", value, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8LessThan(String value) {
            addCriterion("remarks8 <", value, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8LessThanOrEqualTo(String value) {
            addCriterion("remarks8 <=", value, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8Like(String value) {
            addCriterion("remarks8 like", value, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8NotLike(String value) {
            addCriterion("remarks8 not like", value, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8In(List<String> values) {
            addCriterion("remarks8 in", values, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8NotIn(List<String> values) {
            addCriterion("remarks8 not in", values, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8Between(String value1, String value2) {
            addCriterion("remarks8 between", value1, value2, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks8NotBetween(String value1, String value2) {
            addCriterion("remarks8 not between", value1, value2, "remarks8");
            return (Criteria) this;
        }

        public Criteria andRemarks9IsNull() {
            addCriterion("remarks9 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks9IsNotNull() {
            addCriterion("remarks9 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks9EqualTo(String value) {
            addCriterion("remarks9 =", value, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9NotEqualTo(String value) {
            addCriterion("remarks9 <>", value, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9GreaterThan(String value) {
            addCriterion("remarks9 >", value, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9GreaterThanOrEqualTo(String value) {
            addCriterion("remarks9 >=", value, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9LessThan(String value) {
            addCriterion("remarks9 <", value, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9LessThanOrEqualTo(String value) {
            addCriterion("remarks9 <=", value, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9Like(String value) {
            addCriterion("remarks9 like", value, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9NotLike(String value) {
            addCriterion("remarks9 not like", value, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9In(List<String> values) {
            addCriterion("remarks9 in", values, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9NotIn(List<String> values) {
            addCriterion("remarks9 not in", values, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9Between(String value1, String value2) {
            addCriterion("remarks9 between", value1, value2, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks9NotBetween(String value1, String value2) {
            addCriterion("remarks9 not between", value1, value2, "remarks9");
            return (Criteria) this;
        }

        public Criteria andRemarks10IsNull() {
            addCriterion("remarks10 is null");
            return (Criteria) this;
        }

        public Criteria andRemarks10IsNotNull() {
            addCriterion("remarks10 is not null");
            return (Criteria) this;
        }

        public Criteria andRemarks10EqualTo(String value) {
            addCriterion("remarks10 =", value, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10NotEqualTo(String value) {
            addCriterion("remarks10 <>", value, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10GreaterThan(String value) {
            addCriterion("remarks10 >", value, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10GreaterThanOrEqualTo(String value) {
            addCriterion("remarks10 >=", value, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10LessThan(String value) {
            addCriterion("remarks10 <", value, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10LessThanOrEqualTo(String value) {
            addCriterion("remarks10 <=", value, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10Like(String value) {
            addCriterion("remarks10 like", value, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10NotLike(String value) {
            addCriterion("remarks10 not like", value, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10In(List<String> values) {
            addCriterion("remarks10 in", values, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10NotIn(List<String> values) {
            addCriterion("remarks10 not in", values, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10Between(String value1, String value2) {
            addCriterion("remarks10 between", value1, value2, "remarks10");
            return (Criteria) this;
        }

        public Criteria andRemarks10NotBetween(String value1, String value2) {
            addCriterion("remarks10 not between", value1, value2, "remarks10");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}