/*
 * ExtNoSuchMethodException.java: Extended standard throwable for stacks
 *
 * Copyright (C) 2001 Heiko Blau
 *
 * This file belongs to the Susebox Java Core Library (Susebox JCL).
 * The Susebox JCL is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with the Susebox JCL. If not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307
 *   USA
 *
 * or check the Internet: http://www.fsf.org
 *
 * Contact:
 *   email: heiko@susebox.de
 */

package de.susebox.java.lang;

//------------------------------------------------------------------------------
// Imports
//


//------------------------------------------------------------------------------
// ExtNoSuchMethodException - definition
//

/**
 * Implementation of the {@link ThrowableList} interface for the well-known JDK 
 * {@link java.lang.NoSuchMethodException}.
 *
 * @author 	Heiko Blau
 */
public class ExtNoSuchMethodException extends NoSuchMethodException implements ThrowableList
{
  //---------------------------------------------------------------------------
  // methods of the ThrowableList interface
  //
  
  /**
   * Retrieving the cause of a <code>Throwable</code>. This is the method introduced
   * with JDK 1.4. It replaces the older {@link #nextThrowable}.
   *
   * @return the cause of this <code>Throwable</code>
   * @see java.lang.Throwable#getCause
   */
  public Throwable getCause() {
    return _next;
  }
  
  /**
   * Method to traverse the list of {@link java.lang.Throwable}. 
   * See {@link ThrowableList#nextThrowable} for details.
   *
   * @return the "earlier" throwable
   * @deprecated use the JDK 1.4 call {@link #getCause} instead
   */
  public Throwable nextThrowable() {
    return getCause();
  }

  /**
   * Check if <code>this</code> is only a throwable that wraps the real one. See 
   * {@link ThrowableList#isWrapper} for details.
   *
   * @return <code>true</code> if this is a wrapper exception,
   *         <code>false</code> otherwise
   */
  public boolean isWrapper() {
    return _isWrapper;
  }
  
  /**
   * Getting the format string of a exception message. This can also be the 
   * message itself if there are no arguments.
   *
   * @return  the format string being used by {@link java.text.MessageFormat}
   * @see     #getArguments
   */
  public String getFormat() {
    return super.getMessage();
  }
  
  /**
   * Retrieving the arguments for message formats. These arguments are used by
   * the {@link java.text.MessageFormat#format} call.
   *
   * @return  the arguments for a message format
   * @see     #getFormat
   */
  public Object[] getArguments() {
    return _args;
  }
  
  
  //---------------------------------------------------------------------------
  // constructors
  //
  
  /**
   * This constructor takes a simple message string like ordinary Java 
   * {@link java.lang.Throwable} classes. This is the most convenient form to 
   * construct an <code>ThrowableList</code> throwable.
   *
   * @param msg   message for this <code>Throwable</code> instance
   */
  public ExtNoSuchMethodException(String msg) {
    this(null, msg, null);
  }
  
  /**
   * This constructor should be used for wrapping another {@link java.lang.Throwable}. 
   * While reading data an <code>IOException</code> may occur, but a certain interface 
   * requires a <code>SQLException</code>. Simply use:
   *<blockquote><pre>
   * try {
   *   ...
   * } catch (NullPointerException ex) {
   *   throw new ExtNoSuchMethodException(ex);
   * }
   *</pre></blockquote>
   *
   * @param trowable  the <code>Throwable</code> to wrap
   */
  public ExtNoSuchMethodException(Throwable trowable) {
    this(trowable, null, null);
  }
  
  /**
   * If one likes to add ones own information to a {@link java.lang.Throwable}, 
   * this constructor is the easiest way to do so. By using such an approach a 
   * <code>Throwable</code> trace with useful additional informations (which file 
   * could not be found, what username is unknown) can be realized:
   *<blockquote><pre>
   * try {
   *   ...
   * } catch (SQLException ex) {
   *   throw new IOException(ex, "while connecting to " + url);
   * }
   *</pre></blockquote>
   *
   * @param throwable the inner throwable
   * @param msg       throwable message
   */
  public ExtNoSuchMethodException(Throwable throwable, String msg) {
    this(throwable, msg, null);
  }
  
  /**
   * This constructor takes a format string and its arguments. The format string
   * must have a form that can be used by {@link java.text.MessageFormat} methods.
   * That means:
   *<blockquote><pre>
   *    java.text.Message.format(fmt, args)
   *</pre></blockquote>
   * is similar to
   *<blockquote><pre>
   *    new MyException(fmt, args).getMessage();
   *</pre></blockquote>
   *
   * @param fmt   throwable message
   * @param args  arguments for the given format string
   */
  public ExtNoSuchMethodException(String fmt, Object[] args) {
    this(null, fmt, args);
  }
  
  /**
   * This is the most complex way to construct an <code>ThrowableList</code>-
   * Throwable. An inner {@link java.lang.Throwable} is accompanied by a format 
   * string and its arguments.
   *<br>
   * Use this constructor in language-sensitive contexts or for formalized messages.
   * The meaning of the parameters is explained in the other constructors.
   *
   * @param throwable the inner throwable
   * @param fmt       throwable message
   * @param args      arguments for the given format string
   */
  public ExtNoSuchMethodException(Throwable throwable, String fmt, Object[] args) {
    super(fmt);
   
    if (throwable != null && fmt == null) {
      _isWrapper = true;
    } else {
      _isWrapper = false;
    }
    _next = throwable;
    _args = args;
  }
  
  
  //---------------------------------------------------------------------------
  // overridden methods
  //
  
  /**
   * Implementation of the standard {@link java.lang.Throwable#getMessage} method. It
   * delegates the call to the central {@link ThrowableMessageFormatter#getMessage}
   * method.
   *
   * @return  the formatted throwable message
   * @see     ThrowableMessageFormatter
   */
  public String getMessage() {
    return ThrowableMessageFormatter.getMessage(this);
  }
  
  //---------------------------------------------------------------------------
  // members
  //
  
  /**
   * the parameters to be used when formatting the throwable message
   */
  protected Object[]  _args       = null;

  /**
   * The wrapped, nested of next throwable.
   */
  protected Throwable _next       = null;
  
  /**
   * If <code>true</code> this is only a wrapper throwable with the real one
   * being returned by {@link #nextThrowable}, <code>false</code> for standalone, 
   * nested or subsequent exceptions
   */
  protected boolean   _isWrapper  = false;
}