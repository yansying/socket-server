package com.xzr.socket.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author xzr
 */
public class MultiRuleValidator implements ConstraintValidator<MultiRule, Object> {

    private ValidationRule[] validationRules;
    private LogicType logicType;

    @Override
    public void initialize(MultiRule constraintAnnotation) {
        this.validationRules = constraintAnnotation.rules();
        this.logicType = constraintAnnotation.logic();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        for (ValidationRule rule : validationRules) {
            boolean ruleValid = rule.test(object);
            if (logicType == LogicType.AND) {
                if (!ruleValid) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(rule.getErrorMessage()).addConstraintViolation();
                    return false; // 只要有一个不通过，直接返回 false
                }
            } else { // OR 逻辑
                if (ruleValid) {
                    return true; // 只要有一个通过，返回 true
                }
            }
        }

        return logicType == LogicType.AND; // 如果是 AND，返回最后的 valid 状态
    }
}
