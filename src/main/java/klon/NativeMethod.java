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

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeObject(method.getDeclaringClass().getName());
    out.writeObject(method.getName());
    Class[] parameters = method.getParameterTypes();
    out.writeInt(parameters.length);
    for (Class type : parameters) {
      out.writeObject(type.getName());
    }
  }

  private void readObject(ObjectInputStream in) throws IOException,
      ClassNotFoundException, SecurityException, NoSuchMethodException {
    Class type = Class.forName((String) in.readObject());
    String name = (String) in.readObject();
    int size = in.readInt();
    Class[] parameters = new Class[size];
    for (int i = 0; i < size; i++) {
      parameters[i] = Class.forName((String) in.readObject());
    }
    method = type.getMethod(name, parameters);
  }
}
