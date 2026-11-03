package com.lrz.grovecore.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 自增id
 *
 * @author lirenzhi
 * @since 2022/12/9 2:40 下午
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public abstract class IdAutoEntity implements Entity {

    @TableId(type = IdType.AUTO)
    private Long id;
}
