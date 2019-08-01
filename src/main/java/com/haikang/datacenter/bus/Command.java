package com.haikang.datacenter.bus;

/**
 * @author hekun
 * @ClassName: Command
 * @Description: 监听器传输的指令
 * @date 2017年6月26日 下午2:14:27
 */
public class Command {

    //指令ID
    private int code;

    //参数对象
    private Object param;

    //结果
    private Object result;

    //
    private boolean isSync = false;

    //指令来源
    private String source;

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean isSync) {
        this.isSync = isSync;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


    @Override
    public String toString() {
        return "Command [code=" + code + ", param=" + param + ", result=" + result + ", isSync="
                + isSync + ", source=" + source + "]";
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static void main(String[] args) {

    }

}
