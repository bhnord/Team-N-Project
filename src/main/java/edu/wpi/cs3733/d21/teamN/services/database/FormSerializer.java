package edu.wpi.cs3733.d21.teamN.services.database;

import edu.wpi.cs3733.d21.teamN.form.Form;
import java.io.*;

public class FormSerializer {
  public Form fromStream(byte[] stream) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(stream);
      ObjectInputStream ois = new ObjectInputStream(bais);
      return (Form) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  public byte[] toStream(Form f) {
    byte[] stream = null;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(f);
      stream = baos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stream;
  }
}
