package com.yzc.mongo.search;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.yzc.utils.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.query.Criteria;

public class ConditionBridge {
    private static Logger logger = LoggerFactory.getLogger(ConditionBridge.class);

    public ConditionBridge() {
    }

    public static Condition getConditionFromRequest(HttpServletRequest request, Class clazz) throws Exception {
        String filter = StringUtils.AorB(request.getParameter("filter"), request.getParameter("$filter"));
        return processQuery(filter, clazz);
    }

    public static Condition processQuery(String query, Class clazz) throws Exception {
        if (StringUtils.hasText(query)) {
            int lastAndPos = StringUtils.lastMatch("\\band\\b", query);
            int lastOrPos = StringUtils.lastMatch("\\bor\\b", query);
            if (lastAndPos < 0 && lastOrPos < 0) {
                return getConditionFromQueryStr(query, clazz);
            }

            String joinWord = lastAndPos > lastOrPos ? "and" : "or";
            int pos = joinWord.equals("and") ? lastAndPos : lastOrPos;
            String left = query.substring(0, pos).trim();
            String right = query.substring(pos + joinWord.length()).trim();
            Condition leftCondition = processQuery(left, clazz);
            Condition rightCondition = processQuery(right, clazz);
            ArrayList conditions = new ArrayList();
            if (leftCondition != null) {
                conditions.add(leftCondition);
            }

            if (rightCondition != null) {
                conditions.add(rightCondition);
            }

            if (!conditions.isEmpty()) {
                if (joinWord.equals("and")) {
                    return (new Condition()).and(conditions);
                }

                if (joinWord.equals("or")) {
                    return (new Condition()).or(conditions);
                }
            }
        }

        return null;
    }

    public static Condition getConditionFromQueryStr(String queryStr, Class clazz) throws Exception {
        String[] filterValues = queryStr.split("\\s+");
        if (filterValues.length == 3) {
            return buildCondition(StringUtils.underscore2Camel(filterValues[0]), filterValues[1], filterValues[2], clazz);
        } else if (filterValues.length <= 3) {
            return null;
        } else {
            StringBuffer tempValue = new StringBuffer();
            tempValue.append(filterValues[2]);

            for (int i = 3; i < filterValues.length; ++i) {
                tempValue.append(filterValues[i]);
            }

            return buildCondition(StringUtils.underscore2Camel(filterValues[0]), filterValues[1], tempValue.toString(), clazz);
        }
    }

    public static Condition buildCondition(String field, String operator, String value, Class clazz) throws Exception {
        Class fieldType = null;
        if (field.indexOf(".") > -1) {
            int ss = field.indexOf("(");
            if (ss > -1) {
                String i = field.substring(ss + 1, ss + 2);
                if (i.equalsIgnoreCase("I")) {
                    fieldType = Integer.class;
                } else if (i.equalsIgnoreCase("D")) {
                    fieldType = Date.class;
                } else if (i.equalsIgnoreCase("B")) {
                    fieldType = Boolean.class;
                } else if (i.equalsIgnoreCase("F")) {
                    fieldType = Float.class;
                } else if (i.equalsIgnoreCase("L")) {
                    fieldType = Long.class;
                }

                field = field.substring(0, ss);
            }
        } else {
            try {
                Field var9 = clazz.getDeclaredField(field);
                if (var9 != null && var9.getAnnotation(Transient.class) == null) {
                    fieldType = var9.getType();
                }
            } catch (Exception var8) {
                logger.error("没有该字段", var8);
            }
        }

        if (fieldType == null) {
            return null;
        } else {
            Object valueObj;
            if (operator.equals(Operator.in.getValue())) {
                String[] var10 = value.split(",");
                valueObj = new ArrayList();

                for (int var11 = 0; var11 < var10.length; ++var11) {
                    ((List) valueObj).add(convert(var10[var11], fieldType));
                }
            } else {
                valueObj = convert(value, fieldType);
            }

            return new Condition(field, Operator.valueOf(operator), valueObj);
        }
    }

    public static Object convert(String value, Class fieldType) {
        Object valueObj = value;
        if (!fieldType.equals(Integer.class) && !fieldType.equals(Integer.TYPE)) {
            if (fieldType.equals(Boolean.class)) {
                valueObj = Boolean.valueOf(value);
            } else if (fieldType.equals(Date.class)) {
                DateTime dateTime = new DateTime(Long.valueOf(value.toString()));
                valueObj = dateTime.toDate();
            } else if (!fieldType.equals(Long.class) && !fieldType.equals(Long.TYPE)) {
                if (!fieldType.equals(Float.class) && !fieldType.equals(Float.TYPE)) {
                    if (fieldType.equals(Double.class) || fieldType.equals(Double.TYPE)) {
                        valueObj = Double.valueOf(value);
                    }
                } else {
                    valueObj = Float.valueOf(value);
                }
            } else {
                valueObj = Long.valueOf(value);
            }
        } else {
            valueObj = Integer.valueOf(value);
        }

        return valueObj;
    }

    public static Criteria toCriteria(Condition condition) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Criteria criteria = new Criteria();
        if (condition != null) {
            if (condition.selfCondition()) {
                criteria = Criteria.where(condition.getProperty());
                Operator andConditionChain = condition.getOperator();
                Method orConditionChain = null;
                if (andConditionChain.equals(Operator.like)) {
                    orConditionChain = Criteria.class.getDeclaredMethod(andConditionChain.getValue(), new Class[]{String.class});
                } else if (andConditionChain.equals(Operator.in)) {
                    orConditionChain = Criteria.class.getDeclaredMethod(andConditionChain.getValue(), new Class[]{Collection.class});
                } else {
                    orConditionChain = Criteria.class.getDeclaredMethod(andConditionChain.getValue(), new Class[]{Object.class});
                }

                orConditionChain.invoke(criteria, new Object[]{condition.getValue()});
            }

            List andConditionChain1 = condition.getAndConditionChain();
            List orConditionChain1;
            if (andConditionChain1 != null && andConditionChain1.size() > 0) {
                orConditionChain1 = toCriteria(andConditionChain1);
                criteria.andOperator((Criteria[]) orConditionChain1.toArray(new Criteria[orConditionChain1.size()]));
            }

            orConditionChain1 = condition.getOrConditionChain();
            if (orConditionChain1 != null && orConditionChain1.size() > 0) {
                List orCriterias = toCriteria(orConditionChain1);
                criteria.orOperator((Criteria[]) orCriterias.toArray(new Criteria[orCriterias.size()]));
            }
        }

        return criteria;
    }

    public static List<Criteria> toCriteria(List<Condition> conditions) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ArrayList criterias = new ArrayList();
        if (conditions != null && !conditions.isEmpty()) {
            Iterator iterator = conditions.iterator();

            while (iterator.hasNext()) {
                Condition condition = (Condition) iterator.next();
                if (condition != null) {
                    criterias.add(toCriteria(condition));
                }
            }
        }

        return criterias;
    }
}



