// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.api.command.admin.annotation;

import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.NetworkRuleConflictException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.user.Account;
import com.google.common.base.Preconditions;
import org.apache.cloudstack.annotation.AnnotationService;
import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.AnnotationResponse;

/**
 * @since 4.11
 */
@APICommand(name = AddAnnotationCmd.APINAME, description = "add an annotation.", responseObject = AnnotationResponse.class,
        requestHasSensitiveInfo = false, responseHasSensitiveInfo = false)
public class AddAnnotationCmd extends BaseCmd{

    @Parameter(name = ApiConstants.ANNOTATION, type = CommandType.STRING, description = "the annotation text")
    private String annotation;
    @Parameter(name = ApiConstants.ENTITY_TYPE, type = CommandType.STRING, description = "the entity type")
    private String entityType;
    @Parameter(name = ApiConstants.ENTITY_ID, type = CommandType.STRING, description = "the id of the entity to annotate")
    private String entityUuid;

    public String getAnnotation() {
        return annotation;
    }

    public AnnotationService.EntityType getEntityType() {
        return AnnotationService.EntityType.valueOf(entityType);
    }

    public String getEntityUuid() {
        return entityUuid;
    }

    public static final String APINAME = "addAnnotation";

    @Override
    public void execute()
            throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException, ResourceAllocationException,
            NetworkRuleConflictException {
        Preconditions.checkNotNull(entityUuid,"I have to have an entity to set an annotation on!");
        AnnotationResponse annotationResponse = annotationService.addAnnotation(this);
        annotationResponse.setResponseName(getCommandName());
        this.setResponseObject(annotationResponse);
    }

    @Override
    public String getCommandName() {
        return APINAME.toLowerCase() + BaseCmd.RESPONSE_SUFFIX;
    }

    @Override
    public long getEntityOwnerId() {
        // for now all annotations are belong to us
        return Account.ACCOUNT_ID_SYSTEM;
        // we would of course query the owner of the actual entitytype/entityuuid to return the right account
    }
}