/*!
 * @copyright
 * Copyright (c) 2015-2019 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */

#include "agent-framework/module/common_components.hpp"
#include "storage_agent_commands.hpp"

using namespace agent_framework::module;
using namespace agent::storage;

namespace {

void get_managers_collection(GetManagersCollection::ContextPtr,
                             const GetManagersCollection::Request&,
                             GetManagersCollection::Response& response) {
    log_debug("storage-agent", "Getting collection of managers...");
    auto keys = get_manager<agent_framework::model::Manager>().get_keys();
    for (auto key : keys) {
        response.add_entry(agent_framework::model::attribute::ManagerEntry{key});
    }
    log_debug("storage-agent", "Getting managers collection is successfully finished.");
}

}

REGISTER_STORAGE_COMMAND(GetManagersCollection, ::get_managers_collection);
