package com.ecommerce.backend.service;

import java.util.List;
import java.util.Set;

import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.OrderItem;
import com.ecommerce.backend.domain.OrderStatus;
import com.ecommerce.backend.exceptions.OrderException;
import com.ecommerce.backend.exceptions.OrderNotFoundException;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.exceptions.UnauthorizedAcessException;
import com.ecommerce.backend.model.User;

public interface OrderService {

        Set<Order> createOrder(User user, Address shippingAddress, Cart cart);

        Order findOrderById(Long id) throws OrderNotFoundException;

        OrderItem getOrderItemById(Long id) throws OrderNotFoundException, OrderException;

        List<Order> userOrderHistory(Long userId);

        List<Order> sellersOrder(Long sellerId);

        Order updateOrderStatus(
                        Long orderId,
                        Long sellerId,
                        OrderStatus orderStatus)
                        throws OrderNotFoundException, UnauthorizedAcessException, SellerException;

        Order cancelOrder(Long orderId, User user)
                        throws UnauthorizedAcessException, OrderNotFoundException, SellerException;

        //
        List<Order> archivedOrders(Long userId);

        Order archiveOrder(Long orderId, User user)
                        throws UnauthorizedAcessException,
                        OrderNotFoundException;

        Order unArchiveOrder(Long orderId, User user)
                        throws UnauthorizedAcessException,
                        OrderNotFoundException;

}
