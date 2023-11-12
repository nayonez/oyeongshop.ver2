package com.amolli.oyeongshop.ver2.user.model;

import com.amolli.oyeongshop.ver2.product.model.ProductOption;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Builder
@Entity
@Table(name = "tbl_cart_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "prod_opt_id")
    private ProductOption productOption;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @CreationTimestamp
    private LocalDate cartItemDate;

    private Long cartItemAmount;

    public static CartItem createCartItem(Cart cart, ProductOption productOption, Long cartItemAmount){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductOption(productOption);
        cartItem.setCartItemAmount(cartItemAmount);
        return cartItem;
    }

    public void addCartItemAmount(long cartItemAmount){
        this.cartItemAmount += cartItemAmount;
    }
}
