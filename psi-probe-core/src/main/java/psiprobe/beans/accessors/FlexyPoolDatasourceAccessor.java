/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.beans.accessors;

import java.sql.SQLException;
import java.util.List;

import psiprobe.model.DataSourceInfo;

/**
 * Datasource accessor for Flexy Pool wrappers.
 */
public class FlexyPoolDatasourceAccessor implements DatasourceAccessor {

  /** The Constant flexyPoolDataSourceClassName. */
  private static final String FLEXY_POOL_DATASOURCE_CLASS_NAME =
      "com.vladmihalcea.flexypool.FlexyPoolDataSource";

  /** The Constant delegateAccessors. */
  private static final List<String> DELEGATE_ACCESSOR_CLASS_NAMES =
      List.of("psiprobe.beans.accessors.C3P0DatasourceAccessor",
          "psiprobe.beans.accessors.Dbcp2DatasourceAccessor",
          "psiprobe.beans.accessors.HikariCpDatasourceAccessor",
          "psiprobe.beans.accessors.OracleDatasourceAccessor",
          "psiprobe.beans.accessors.OracleUcpDatasourceAccessor",
          "psiprobe.beans.accessors.OpenEjbBasicDatasourceAccessor",
          "psiprobe.beans.accessors.OpenEjbManagedDatasourceAccessor",
          "psiprobe.beans.accessors.Tomcat10DbcpDatasourceAccessor",
          "psiprobe.beans.accessors.Tomcat11DbcpDatasourceAccessor",
          "psiprobe.beans.accessors.TomcatJdbcPoolDatasourceAccessor",
          "psiprobe.beans.accessors.TomEeJdbcPoolDatasourceAccessor",
          "psiprobe.beans.accessors.ViburCpDatasourceAccessor");

  /** The Constant delegateAccessors. */
  private static final List<DatasourceAccessor> DELEGATE_ACCESSORS =
      DELEGATE_ACCESSOR_CLASS_NAMES.stream().map(FlexyPoolDatasourceAccessor::newAccessor).toList();

  @Override
  public DataSourceInfo getInfo(Object resource) throws SQLException {
    Object targetDataSource = getTargetDataSource(resource);
    if (targetDataSource == null) {
      return null;
    }

    for (DatasourceAccessor accessor : DELEGATE_ACCESSORS) {
      if (accessor.canMap(targetDataSource)) {
        return accessor.getInfo(targetDataSource);
      }
    }

    return null;
  }

  @Override
  public boolean reset(Object resource) throws SQLException {
    Object targetDataSource = getTargetDataSource(resource);
    if (targetDataSource == null) {
      return false;
    }

    for (DatasourceAccessor accessor : DELEGATE_ACCESSORS) {
      if (accessor.canMap(targetDataSource)) {
        return accessor.reset(targetDataSource);
      }
    }

    return false;
  }

  @Override
  public boolean canMap(Object resource) {
    return resource != null
        && FLEXY_POOL_DATASOURCE_CLASS_NAME.equals(resource.getClass().getName());
  }

  /**
   * Gets the target datasource from a Flexy Pool wrapper.
   *
   * @param resource the resource
   * @return the target datasource
   */
  private Object getTargetDataSource(Object resource) {
    if (!canMap(resource)) {
      return null;
    }

    try {
      return resource.getClass().getMethod("getTargetDataSource").invoke(resource);
    } catch (ReflectiveOperationException e) {
      return null;
    }
  }

  /**
   * Creates a datasource accessor.
   *
   * @param accessorClassName the accessor class name
   * @return the datasource accessor
   */
  private static DatasourceAccessor newAccessor(String accessorClassName) {
    try {
      return Class.forName(accessorClassName).asSubclass(DatasourceAccessor.class)
          .getDeclaredConstructor().newInstance();
    } catch (ReflectiveOperationException e) {
      return new UnsupportedDatasourceAccessor();
    }
  }

  /**
   * Unsupported datasource accessor.
   */
  private static final class UnsupportedDatasourceAccessor implements DatasourceAccessor {

    @Override
    public DataSourceInfo getInfo(Object resource) {
      return null;
    }

    @Override
    public boolean reset(Object resource) {
      return false;
    }

    @Override
    public boolean canMap(Object resource) {
      return false;
    }
  }
}
