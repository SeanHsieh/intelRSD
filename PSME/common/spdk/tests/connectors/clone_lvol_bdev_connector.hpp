/*!
 * @copyright
 * Copyright (c) 2018-2019 Intel Corporation
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
 * @file clone_lvol_bdev_connector.hpp
 */

#include "json-rpc/json_rpc_response.hpp"
#include "json-rpc/connectors/abstract_client_connector.hpp"
#include "json-rpc/handlers/json_rpc_request_invoker.hpp"
#include "spdk/model/bdev.hpp"
#include "spdk/model/lvol_store.hpp"



namespace spdk {

class CloneLvolBdevConnector : public json_rpc::AbstractClientConnector {

public:

    // Container for new created lvol_bdev.
    static model::Bdev m_lvol_bdev_snapshot;

    // Container for lvol_bdev_clone.
    static spdk::JsonOptional<model::Bdev> m_lvol_bdev_clone;


    CloneLvolBdevConnector();


    virtual std::string send_request(const std::string& message) override;

};

}