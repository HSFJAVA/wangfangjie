import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/table/list',
    method: 'get',
    params
  })
}

export function gettableList() {
  return request({
    url: 'kwe/index/searchCustom',
    method: 'get'
  })
}
