package com.lrz.grovecore.core;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lrz.grovecore.envm.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 创建时间更新时间
 *
 * @author lirenzhi
 * @since 2022/12/9 2:42 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
public abstract class AuditableIdAutoEntity extends IdAutoEntity {

    @JsonFormat(pattern = Constant.DateFormat.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;
    @JsonFormat(pattern = Constant.DateFormat.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime updateTime;
    @TableField(value = "is_delete")
    @TableLogic(delval = "1", value = "0")
    private Integer isDelete;
}
