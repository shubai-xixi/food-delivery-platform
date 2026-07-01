package com.elm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 操作日志实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("operate_log")
public class OperateLog {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 操作人 */
    @TableField("operate_user")
    private String operateUser;

    /** 操作时间 */
    @TableField("operate_time")
    private String operateTime;

    /** 类名 */
    @TableField("class_name")
    private String className;

    /** 方法名 */
    @TableField("method_name")
    private String methodName;

    /** 方法参数 */
    @TableField("method_params")
    private String methodParams;

    /** 返回值 */
    @TableField("return_value")
    private String returnValue;

    /** 耗时（毫秒） */
    @TableField("cost_time")
    private Long costTime;
}