package klon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NativeMethod implements Serializable {

  private static final long serialVersionUID = 8122525060995437616L;
  private Method method;

  public NativeMethod(Method method) {
    this.method = method;
  }

  public KlonObject invoke(KlonObject receiver, KlonObject context,
      Message message) throws IllegalArgumentException, IllegalAccessException,
      InvocationTargetException {
    return (KlonObject) method.invoke(null, receiver, context, message);
  }

  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.writeObject(method.getDeclaringClass()
      .getName());
    stream.writeObject(method.getName());
    Class[] types = method.getParameterTypes();
    stream.writeInt(types.length);
    for (Class type : types) {
      stream.writeObject(type.getName());
    }
  }

  private void readObject(ObjectInputStream stream) throws IOException {
    try {
      Class decl = Class.forName((String) stream.readObject());
      String metName = (String) stream.readObject();
      int ln = stream.readInt();
      Class[] sig = new Class[ln];
      for (int i = 0; i < ln; i++) {
        sig[i] = Class.forName((String) stream.readObject());
      }
      method = decl.getMethod(metName, sig);
    } catch (NoSuchMethodException error) {
      System.out.println("NATIVE SYSTEM ERROR IN READING METHOD(nosuchmethod:PrimMethAttribute)");
    } catch (ClassNotFoundException error) {
      System.out.println("NATIVE SYSTEM ERROR IN READING METHOD(nosuchclass:PrimMethAttribute)");
    }
  }
}
