package com.tortuousroad.admin.order.router;

import com.tortuousroad.admin.base.controller.AjaxResult;
import com.tortuousroad.admin.base.router.BaseRouter;
import com.tortuousroad.framework.base.entity.BaseEntity;
import com.tortuousroad.groupon.order.constant.OrderConstant;
import com.tortuousroad.groupon.order.entity.Order;
import com.tortuousroad.groupon.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * OrderRouter
 */
@Component
public class OrderRouter extends BaseRouter {
	
	@Autowired private OrderService orderService;

	public OrderRouter() {
		super.addMethodDisplayName("deliver", "发货");
		super.addMethodDisplayName("complete", "完成");
//		super.addMethodDisplayName("backComplete", "退货完成");
		super.addMethodDisplayName("delete", "删除");
		super.addMethodDisplayName("reorder", "重下单");
		super.addMethodDisplayName("cancel", "取消");
		super.clazz = Order.class;
	}

	/* (non-Javadoc)
	 * @see com.tortuousroad.admin.base.router.BaseRouter#loadEntity(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Order loadEntity(Long id) {
		return this.orderService.getOrderAndDetailByOrderId(id);
	}

	public boolean isButtonDisabled(BaseEntity entity, String method) {
		Order order = (Order) entity;
		switch (method) {
			case "deliver" :
				return (order.getOrderStatus() != OrderConstant.STATUS_WAITING_DELIVER);
			case "complete" :
				return (order.getOrderStatus() != OrderConstant.STATUS_DELIVERING);
			case "delete" :
				return (order.getOrderStatus() != OrderConstant.STATUS_FINISHED
						&& order.getOrderStatus() != OrderConstant.STATUS_CANCELED
						&& order.getOrderStatus() != OrderConstant.STATUS_CLOSED);
			case "reorder":
				return  (order.getOrderStatus() != OrderConstant.STATUS_FINISHED
						&& order.getOrderStatus() != OrderConstant.STATUS_CANCELED
						&& order.getOrderStatus() != OrderConstant.STATUS_CLOSED);
			case "cancel":
				return  (order.getOrderStatus() != OrderConstant.STATUS_WAITING_PAY
						&& order.getOrderStatus() != OrderConstant.STATUS_ALREADY_PAID
						&& order.getOrderStatus() != OrderConstant.STATUS_DELIVERING);
			default:
				return false;
		}
	}

	public AjaxResult delete(BaseEntity user, Order order, Map<String, String> params) {
		return new AjaxResult();
	}

	/**
	 * 取消订单
	 * @param user
	 * @param params
	 * @return
	 */
	public AjaxResult cancel(BaseEntity user, Order order, Map<String, String> params) {
		this.orderService.cancel(order.getId());
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatusCode(AjaxResult.AJAX_STATUS_CODE_SUCCESS);
		return ajaxResult;
	}



	/**
	 * "发货"操作,对应订单状态"配送中"
	 * @param user
	 * @param order
	 * @param params
     * @return
     */
	public AjaxResult deliver(BaseEntity user, Order order, Map<String, String> params) {
		this.orderService.deliver(order.getId());
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatusCode(AjaxResult.AJAX_STATUS_CODE_SUCCESS);
		return ajaxResult;
	}

	/**
	 * "完成"操作,,对应订单状态"完成"
	 * @param user
	 * @param order
	 * @param params
	 * @return
	 */
	public AjaxResult complete(BaseEntity user, Order order, Map<String, String> params) {
		this.orderService.complete(order.getId());
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatusCode(AjaxResult.AJAX_STATUS_CODE_SUCCESS);
		return ajaxResult;
	}

}