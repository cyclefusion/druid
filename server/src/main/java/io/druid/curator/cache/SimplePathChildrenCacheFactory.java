/*
 * Druid - a distributed column store.
 * Copyright 2012 - 2015 Metamarkets Group Inc.
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

package io.druid.curator.cache;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.utils.ThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 */
public class SimplePathChildrenCacheFactory implements PathChildrenCacheFactory
{
  private final boolean cacheData;
  private final boolean compressed;
  private final ExecutorService exec;

  public SimplePathChildrenCacheFactory(
      boolean cacheData,
      boolean compressed,
      ExecutorService exec
  )
  {
    this.cacheData = cacheData;
    this.compressed = compressed;
    this.exec = exec;
  }

  @Override
  public PathChildrenCache make(CuratorFramework curator, String path)
  {
    return new PathChildrenCache(curator, path, cacheData, compressed, exec);
  }

  public static class Builder
  {
    private static final ThreadFactory defaultThreadFactory = ThreadUtils.newThreadFactory("PathChildrenCache");

    private boolean cacheData;
    private boolean compressed;
    private ExecutorService exec;

    public Builder()
    {
      cacheData = true;
      compressed = false;
      exec = Executors.newSingleThreadExecutor(defaultThreadFactory);
    }

    public Builder withCacheData(boolean cacheData)
    {
      this.cacheData = cacheData;
      return this;
    }

    public Builder withCompressed(boolean compressed)
    {
      this.compressed = compressed;
      return this;
    }

    public Builder withExecutorService(ExecutorService exec)
    {
      this.exec = exec;
      return this;
    }

    public SimplePathChildrenCacheFactory build()
    {
      return new SimplePathChildrenCacheFactory(cacheData, compressed, exec);
    }
  }
}
