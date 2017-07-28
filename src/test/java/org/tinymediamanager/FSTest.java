package org.tinymediamanager;

import java.net.URISyntaxException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class FSTest {

  @Test
  public void testFS() throws URISyntaxException {
    FileSystem fileSystem = FileSystems.getDefault();
    Iterable<FileStore> fileStores = fileSystem.getFileStores();
    for (FileStore fileStore : fileStores) {
      System.out.println(String.format("Filestore %s supports %s", fileStore, getSupportedFileAttributes(fileStore)));
    }
  }

  private Set<String> getSupportedFileAttributes(FileStore fs) {
    Set<String> attrs = new HashSet<String>();
    if (fs.supportsFileAttributeView(AclFileAttributeView.class)) {
      attrs.add("acl");
    }
    if (fs.supportsFileAttributeView(BasicFileAttributeView.class)) {
      attrs.add("basic");
    }
    if (fs.supportsFileAttributeView(FileOwnerAttributeView.class)) {
      attrs.add("owner");
    }
    if (fs.supportsFileAttributeView(UserDefinedFileAttributeView.class)) {
      attrs.add("user");
    }
    if (fs.supportsFileAttributeView(DosFileAttributeView.class)) {
      attrs.add("dos");
    }
    if (fs.supportsFileAttributeView(PosixFileAttributeView.class)) {
      attrs.add("posix");
    }
    if (fs.supportsFileAttributeView(FileAttributeView.class)) {
      attrs.add("file");
    }
    return attrs;
  }
}
