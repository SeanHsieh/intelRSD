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

package com.intel.rsd.nodecomposer.persistence.redfish;

import com.intel.rsd.nodecomposer.persistence.redfish.base.DiscoverableEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.intel.rsd.nodecomposer.utils.Contracts.requiresNonNull;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@javax.persistence.Entity
@Table(name = "ethernet_switch")
public class EthernetSwitch extends DiscoverableEntity {
    @OneToMany(mappedBy = "ethernetSwitch", fetch = LAZY, cascade = {MERGE, PERSIST})
    private Set<EthernetSwitchPort> ports = new HashSet<>();

    @ManyToOne(fetch = LAZY, cascade = {MERGE, PERSIST})
    @JoinColumn(name = "chassis_id")
    private Chassis chassis;

    public Set<EthernetSwitchPort> getPorts() {
        return ports;
    }

    public void addPort(EthernetSwitchPort ethernetSwitchPort) {
        requiresNonNull(ethernetSwitchPort, "ethernetSwitchPort");

        ports.add(ethernetSwitchPort);
        if (!this.equals(ethernetSwitchPort.getEthernetSwitch())) {
            ethernetSwitchPort.setEthernetSwitch(this);
        }
    }

    public void unlinkPort(EthernetSwitchPort ethernetSwitchPort) {
        if (ports.contains(ethernetSwitchPort)) {
            ports.remove(ethernetSwitchPort);
            if (ethernetSwitchPort != null) {
                ethernetSwitchPort.unlinkEthernetSwitch(this);
            }
        }
    }

    public Chassis getChassis() {
        return chassis;
    }

    public void setChassis(Chassis chassis) {
        if (!Objects.equals(this.chassis, chassis)) {
            unlinkChassis(this.chassis);
            this.chassis = chassis;
            if (chassis != null && !chassis.getEthernetSwitches().contains(this)) {
                chassis.addEthernetSwitch(this);
            }
        }
    }

    public void unlinkChassis(Chassis chassis) {
        if (Objects.equals(this.chassis, chassis)) {
            this.chassis = null;
            if (chassis != null) {
                chassis.unlinkEthernetSwitch(this);
            }
        }
    }

    @Override
    public void preRemove() {
        unlinkCollection(ports, this::unlinkPort);
        unlinkChassis(chassis);
    }
}
