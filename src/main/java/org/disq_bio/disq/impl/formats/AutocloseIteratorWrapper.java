/*
 * Disq
 *
 * MIT License
 *
 * Copyright (c) 2018-2019 Disq contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.disq_bio.disq.impl.formats;

import htsjdk.samtools.util.RuntimeIOException;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * An {@link Iterator} that automatically closes a resource when the end of the iteration is
 * reached.
 *
 * @param <E> element type
 */
public class AutocloseIteratorWrapper<E> implements Iterator<E> {

  private final Iterator<E> iterator;
  private final Closeable closeable;

  public AutocloseIteratorWrapper(Iterator<E> iterator, Closeable closeable) {
    this.iterator = iterator;
    this.closeable = closeable;
  }

  @Override
  public boolean hasNext() {
    boolean hasNext = iterator.hasNext();
    if (!hasNext) {
      try {
        closeable.close();
      } catch (IOException e) {
        throw new RuntimeIOException(e);
      }
    }
    return hasNext;
  }

  @Override
  public E next() {
    return iterator.next();
  }

  @Override
  public void remove() {
    iterator.remove();
  }

  @Override
  public void forEachRemaining(Consumer<? super E> action) {
    iterator.forEachRemaining(action);
  }
}
