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

  public Object invoke(Object... args) throws IllegalArgumentException,
      IllegalAccessException, InvocationTargetException {
    return method.invoke(null, args);
  }

  @Override
  public String toString() {
    return method.toString();
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

  private void readObject(ObjectInputStream stream) throws IOException,
      ClassNotFoundException, SecurityException, NoSuchMethodException {
    Class decl = Class.forName((String) stream.readObject());
    String metName = (String) stream.readObject();
    int ln = stream.readInt();
    Class[] sig = new Class[ln];
    for (int i = 0; i < ln; i++) {
      sig[i] = Class.forName((String) stream.readObject());
    }
    method = decl.getMethod(metName, sig);
  }
}
