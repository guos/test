
package mtl.wabi.db.proxy.common.db;

import java.util.concurrent.LinkedBlockingDeque;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public final class DynamicDataSourceContextHolder {

	/**
	 * 为什么要用链表存储(准确的是栈)
	 * 
	 * <pre>
	 * 为了支持嵌套切换，如ABC三个service都是不同的数据源
	 * 其中A的某个业务要调B的方法，B的方法需要调用C的方法。一级一级调用切换，形成了链。
	 * 传统的只设置当前线程的方式不能满足此业务需求，必须模拟栈，后进先出。
	 * </pre>
	 */

	private static final ThreadLocal<LinkedBlockingDeque<String>> LOOKUP_KEY_HOLDER = new ThreadLocal<LinkedBlockingDeque<String>>() {
		@Override
		protected LinkedBlockingDeque<String> initialValue() {
			return new LinkedBlockingDeque<String>();
		}
	};

	private DynamicDataSourceContextHolder() {
	}

	/**
	 * 获得当前线程数据源
	 *
	 * @return 数据源名称
	 */
	public static String getDataSourceLookupKey() {
		LinkedBlockingDeque<String> deque = LOOKUP_KEY_HOLDER.get();
		String keyString= deque.isEmpty() ? null : deque.getFirst();
		log.info("Got the current thread's dataSource key[{}]",keyString);
		return keyString;
	}

	/**
	 * current datasource key
	 *
	 * @param dataSourceLookupKey 
	 */
	public static void setDataSourceLookupKey(String dataSourceLookupKey) {
		log.info("Set the dataSource key is [{}]",dataSourceLookupKey);
		LOOKUP_KEY_HOLDER.get().addFirst(dataSourceLookupKey);
	}

	/**
	 * 清空当前线程数据源
	 * <p>
	 * 如果当前线程是连续切换数据源 只会移除掉当前线程的数据源名称
	 * </p>
	 */
	public static void clearDataSourceLookupKey() {
		LinkedBlockingDeque<String> deque = LOOKUP_KEY_HOLDER.get();
		if (deque.isEmpty()) {
			LOOKUP_KEY_HOLDER.remove();
		} else {
			deque.pollFirst();
		}
	}
}
