/*
 * Copyright (c) 2016-2019 Intel Corporation
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

package com.intel.rsd.nodecomposer.mappers.redfish;

import com.intel.rsd.nodecomposer.externalservices.resources.redfish.FabricResource;
import com.intel.rsd.nodecomposer.mappers.EntityMapper;
import com.intel.rsd.nodecomposer.persistence.redfish.Fabric;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.intel.rsd.nodecomposer.types.Protocol.OEM;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class FabricMapper extends EntityMapper<FabricResource, Fabric> {
    protected FabricMapper() {
        super(FabricResource.class, Fabric.class);
    }

    @Override
    public void performNotAutomatedMapping(FabricResource resource, Fabric target) {
        super.performNotAutomatedMapping(source, target);
        if (OEM.equals(resource.getFabricType())) {
            target.setFabricType(resource.getFabricTypeFromOem());
        }
    }
}
