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

export function searchFilesByType(postfix:Array<String>,page : number,lastPath : String)
: Promise<ReturnType<any>>{
  return Native.callNativeWithPromise(MODULE_NAME, 'searchFilesByType',[[postfix,page,lastPath]]);
}

export default {
    connectSMBServer,
    getLocalIP,
    searchFilesByType
}
