package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Set;

public class IsInSetConstraint implements IConstraint
{
    public final Field field;
    public final Set<Object> legalValues;

    public IsInSetConstraint(Field field, Set<Object> legalValues) {
        this.field = field;
        this.legalValues = legalValues;
    }
}
