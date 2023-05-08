function exportTeachInfo(teachInfoList) {
    return axios({
        method: 'post',
        url: '/api/v1/excel/teachInfo',
        responseType: 'blob',
        data: teachInfoList
    })
}

function exportTeachInfo2(teachInfo2List) {
    return axios({
        method: 'post',
        url: '/api/v1/excel/teachInfo2',
        responseType: 'blob',
        data: teachInfo2List
    })
}