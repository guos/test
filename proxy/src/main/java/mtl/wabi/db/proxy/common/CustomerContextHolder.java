package mtl.wabi.db.proxy.common;
@Deprecated
public class CustomerContextHolder {
	private static final ThreadLocal<String> contextHolder =  new ThreadLocal<String>();
	
   public static void setDataSource(String String) {
    
      contextHolder.set(String);
   }

   public static String getDataSource() {
      return  contextHolder.get();
   }

   public static void clearDataSource() {
      contextHolder.remove();
   }
}
