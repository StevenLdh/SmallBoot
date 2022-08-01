package com.supper.smallboot.domain.event;
import com.handday.formless.framework.event.AbstractEvent;
import lombok.Data;

/**
 * @Author ldh
 * @Description
 * @Date 14:01 2022-08-01
 **/
@Data
public class DemoEvent extends AbstractEvent{
    public int customerQty;
    public Long corpId;

    public DemoEvent(Long corpId,int customerQty){
        this.corpId=corpId;
        this.customerQty=customerQty;
    }
}
