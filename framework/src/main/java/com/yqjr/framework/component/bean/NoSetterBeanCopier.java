package com.yqjr.framework.component.bean;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.Local;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;

/**
 * ClassName: NoSetterBeanCopier <br>
 * Description: 扩展cglib的BeanCopier内容,当setter方法不存在时忽略该属性,而非抛出异常 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月26日 上午11:48:17 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public abstract class NoSetterBeanCopier extends BeanCopier {

	private static final Type CONVERTER = TypeUtils.parseType("net.sf.cglib.core.Converter");
	private static final Type BEAN_COPIER = TypeUtils.parseType("net.sf.cglib.beans.BeanCopier");
	private static final Signature COPY = new Signature("copy", Type.VOID_TYPE,
			new Type[] { Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER });
	private static final Signature CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");

	@SuppressWarnings("rawtypes")
	public static BeanCopier create(Class source, Class target, boolean useConverter) {
		NoSetterGenerator gen = new NoSetterGenerator();
		gen.setSource(source);
		gen.setTarget(target);
		gen.setUseConverter(useConverter);
		return gen.create();
	}

	@SuppressWarnings("rawtypes")
	public static class NoSetterGenerator extends Generator {

		private Class source;
		private Class target;
		private boolean useConverter;

		public void setSource(Class source) {
			super.setSource(source);
			this.source = source;
		}

		public void setTarget(Class target) {
			super.setTarget(target);
			this.target = target;
		}

		public void setUseConverter(boolean useConverter) {
			super.setUseConverter(useConverter);
			this.useConverter = useConverter;
		}

		@SuppressWarnings("unchecked")
		public void generateClass(ClassVisitor v) {
			Type sourceType = Type.getType(source);
			Type targetType = Type.getType(target);
			ClassEmitter ce = new ClassEmitter(v);
			ce.begin_class(Constants.V1_2, Constants.ACC_PUBLIC, getClassName(), BEAN_COPIER, null,
					Constants.SOURCE_FILE);

			EmitUtils.null_constructor(ce);
			CodeEmitter e = ce.begin_method(Constants.ACC_PUBLIC, COPY, null);
			PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(source);
			PropertyDescriptor[] setters = ReflectUtils.getBeanGetters(target);

			Map names = new HashMap();
			for (int i = 0; i < getters.length; i++) {
				names.put(getters[i].getName(), getters[i]);
			}
			Local targetLocal = e.make_local();
			Local sourceLocal = e.make_local();
			if (useConverter) {
				e.load_arg(1);
				e.checkcast(targetType);
				e.store_local(targetLocal);
				e.load_arg(0);
				e.checkcast(sourceType);
				e.store_local(sourceLocal);
			} else {
				e.load_arg(1);
				e.checkcast(targetType);
				e.load_arg(0);
				e.checkcast(sourceType);
			}
			for (int i = 0; i < setters.length; i++) {
				PropertyDescriptor setter = setters[i];
				PropertyDescriptor getter = (PropertyDescriptor) names.get(setter.getName());
				// 增加判断条件,如果setter为空则不进入
				if (getter != null && setter.getWriteMethod() != null) {
					MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
					MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
					if (useConverter) {
						Type setterType = write.getSignature().getArgumentTypes()[0];
						e.load_local(targetLocal);
						e.load_arg(2);
						e.load_local(sourceLocal);
						e.invoke(read);
						e.box(read.getSignature().getReturnType());
						EmitUtils.load_class(e, setterType);
						e.push(write.getSignature().getName());
						e.invoke_interface(CONVERTER, CONVERT);
						e.unbox_or_zero(setterType);
						e.invoke(write);
					} else if (compatible(getter, setter)) {
						e.dup2();
						e.invoke(read);
						e.invoke(write);
					}
				}
			}
			e.return_value();
			e.end_method();
			ce.end_class();
		}

		private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
			return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
		}

	}

}
