/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.admin.indices.mapping.get;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.master.info.TransportClusterInfoAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

/**
 */
public class TransportGetMappingsAction extends TransportClusterInfoAction<GetMappingsRequest, GetMappingsResponse> {

    @Inject
    public TransportGetMappingsAction(Settings settings, TransportService transportService, ClusterService clusterService, ThreadPool threadPool, ActionFilters actionFilters) {
        super(settings, GetMappingsAction.NAME, transportService, clusterService, threadPool, actionFilters);
    }

    @Override
    protected GetMappingsRequest newRequest() {
        return new GetMappingsRequest();
    }

    @Override
    protected GetMappingsResponse newResponse() {
        return new GetMappingsResponse();
    }

    @Override
    protected void doMasterOperation(final GetMappingsRequest request, String[] concreteIndices, final ClusterState state, final ActionListener<GetMappingsResponse> listener) throws ElasticsearchException {
        logger.trace("serving getMapping request based on version {}", state.version());
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> result = state.metaData().findMappings(
                concreteIndices, request.types()
        );
        listener.onResponse(new GetMappingsResponse(result));
    }
}
