/*
 * Copyright 2012 - 2018 Manuel Laggner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinymediamanager.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.SwingPropertyChangeSupport;

/**
 * The Class AbstractModelObject.
 * 
 * @author Manuel Laggner
 */
public abstract class AbstractModelObject {

  /** The property change support. */
  // private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

  private final PropertyChangeSupport propertyChangeSupport = new SwingPropertyChangeSupport(this, true);

  /**
   * Adds the property change listener.
   * 
   * @param listener
   *          the listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Adds the property change listener.
   * 
   * @param propertyName
   *          the property name
   * @param listener
   *          the listener
   */
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * Removes the property change listener.
   * 
   * @param listener
   *          the listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Removes the property change listener.
   * 
   * @param propertyName
   *          the property name
   * @param listener
   *          the listener
   */
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    try {
      propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
    catch (AssertionError e) {
    }
  }

  /**
   * Fire property change.
   * 
   * @param propertyName
   *          the property name
   * @param oldValue
   *          the old value
   * @param newValue
   *          the new value
   */
  public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    try {
      propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    catch (AssertionError e) {
    }
  }

  /**
   * Fire property change.
   * 
   * @param evt
   *          the evt
   */
  public void firePropertyChange(PropertyChangeEvent evt) {
    propertyChangeSupport.firePropertyChange(evt);
  }
}
