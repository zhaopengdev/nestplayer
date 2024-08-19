import {Native} from "@extscreen/es3-vue";

const MODULE_NAME = 'ClientModule';

export function connectSMBServer(ip:string,userName:string,
  password:string) : Promise<ReturnType<any>>{
    return Native.callNativeWithPromise(MODULE_NAME, 'connectServer',
      ['smb',ip,userName,password]);
}

export function getLocalIP() : Promise<ReturnType<any>>{
    return Native.callNativeWithPromise(MODULE_NAME, 'getLocalIP', []);
}

export function searchFilesByType(path:string,type:string,page:number,pageSize:number)
: Promise<ReturnType<any>>{
  return Native.callNativeWithPromise(MODULE_NAME, 'searchFilesByType',[path,type,page,pageSize]);
}

export default {
    connectSMBServer,
    getLocalIP,
    searchFilesByType
}
