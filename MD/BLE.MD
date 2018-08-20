```java
try {
            Method removeBondMethod = BluetoothDevice.class.getMethod("removeBond");
            Log4Android.d(this, "取消配对:" + data.name + "|" + data.address);
            boolean success = (boolean)removeBondMethod.invoke(data);
            Log4Android.d(this, "取消配对:" + data.name + "|" + success);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
        
 /*try {
 
             Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
             Log4Android.d(this, "开始配对:" + data.getName() + "|" + data.getAddress());
             boolean boundResult = (boolean)createBondMethod.invoke(data);
 
             Method setPin = BluetoothDevice.class.getDeclaredMethod("setPin", new Class[]{byte[].class});
             boolean setPinResult = (boolean) setPin.invoke(data, new Object[]{"0000".getBytes()});
             Log4Android.d(this, "自动设置PIN码:0000|" + setPinResult);
 
             boundResult = (boolean)createBondMethod.invoke(data);
 
             Log4Android.d(this, "配对:" + data.getName() + "|" + boundResult);
 
 
 
         } catch (NoSuchMethodException e) {
             e.printStackTrace();
         } catch (InvocationTargetException e) {
             e.printStackTrace();
         } catch (IllegalAccessException e) {
             e.printStackTrace();
         }*/

```