/* Copyright 2005-2006 Tim Fennell
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
package net.sourceforge.stripes.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Encapsulates the name of a parameter in the HttpServletRequest. Detects whether or
 * not the name refers to an indexed or mapped property.
 *
 * @author Tim Fennell
 */
public class ParameterName implements Comparable<ParameterName> {

   /** Stores the regular expression that will remove all [] segments. */
   public static final Pattern pattern = Pattern.compile("\\[.*?\\]");

   /** Stores the name passed in at construction time. */
   private final String _name;

   /** Stores the name with all indexing and mapping stripped out of it. */
   private final String _strippedName;

   /** True if the name has indexing or mapping in it. */
   private final boolean _indexed;

   /**
    * Constructs a ParameterName for a given name from the HttpServletRequest. As it is
    * constructed, detects whether or not the name contains indexing or mapping components,
    * and if it does, also creates and stores the stripped name.
    *
    * @param name a name that may or may not contain indexing or mapping
    */
   public ParameterName( String name ) {
      _name = name;
      Matcher matcher = pattern.matcher(_name);
      _indexed = matcher.find();

      if ( _indexed ) {
         _strippedName = matcher.replaceAll("");
      } else {
         _strippedName = _name;
      }
   }

   /**
    * Orders ParameterNames so that those with shorter (unstripped) names come first. Two
    * names of the same length are then ordered alphabetically by String.compareTo().
    *
    * @param that another ParameterName to compare to
    * @return -1 if this value sorts first, 0 if the values are identical and +1 if the
    *         parameter passed in sorts first.
    */
   @Override
   public int compareTo( ParameterName that ) {
      int result = Integer.compare(_name.length(), that._name.length());
      if ( result == 0 ) {
         result = _name.compareTo(that._name);
      }

      return result;
   }

   /**
    * Checks for equality as efficiently as possible.  First checks for JVM equality to
    * see if we can short circuit, and then checks for equality of the name attribute for
    * a real test.
    */
   @Override
   public boolean equals( Object obj ) {
      return (obj instanceof ParameterName) && (this == obj || compareTo((ParameterName)obj) == 0);
   }

   /**
    * Always returns the parameter name as passed in to the constructor. If it contained
    * indexing or mapping components (e.g. [3] or (foo)) they will be present in the
    * String returned.
    *
    * @return String the name as supplied in the request
    */
   public String getName() {
      return _name;
   }

   /**
    * Returns the name with all indexing and mapping components stripped.  E.g. if the name
    * in the request was 'foo[1].bar', this method will return 'foo.bar'.
    *
    * @return String the name minus indexing and mapping
    */
   public String getStrippedName() {
      return _strippedName;
   }

   /** Simple hashcode method based on the name of the parameter. */
   @Override
   public int hashCode() {
      return _name.hashCode();
   }

   /** Returns true if the name has indexing or mapping components, otherwise false. */
   public boolean isIndexed() {
      return _indexed;
   }

   /**
    * Uses the original name as the string representation of the class. This is probably
    * the most useful thing to see in log messages, which is the only place toString will
    * be used.
    */
   @Override
   public String toString() {
      return _name;
   }
}
