"""
 * @section LICENSE
 *
 * @copyright
 * Copyright (c) 2019 Intel Corporation
 *
 * @copyright
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * @copyright
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * @copyright
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @section DESCRIPTION
"""
import sys

from cts_core.commons.error import cts_error
from cts_core.metadata.model.metadata_types.primitive_types.primitive_type import PrimitiveType
from cts_core.validation.validation_status import ValidationStatus


@PrimitiveType.register(".".join([PrimitiveType.EDM, "Double"]))
class Double(PrimitiveType):
    def __init__(self):
        PrimitiveType.__init__(self)

    def validate(self, resource, resource_path, annotations=None):
        """
        :type annotations: dict
        :type resource: float
        :type resource_path: str
        :rtype: str
        """
        if annotations is None:
            annotations = dict()

        if type(resource) not in [int, float]:
            cts_error("Value of property {resource_path} is not double type", resource_path=resource_path)
            return ValidationStatus.FAILED

        if "Validation.Minimum" in annotations:
            minimum = float(annotations["Validation.Minimum"].value)
        else:
            minimum = float('-inf')

        if "Validation.Maximum" in annotations:
            maximum = float(annotations["Validation.Maximum"].value)
        else:
            maximum = float('inf')

        if minimum <= resource <= maximum:
            return ValidationStatus.PASSED
        else:
            cts_error("Value {value} of property {resource_path} does not match range <{minimum}, {maximum}>.",
                      value=str(resource), resource_path=resource_path, minimum=str(minimum), maximum=str(maximum))
            return ValidationStatus.FAILED

    def generate_values(self, annotations):
        """
        :type annotations: dict [str, cts_core.metadata.model.annotation.Annotation]
        :rtype: list
        """
        if "Validation.Minimum" in annotations:
            minimum = float(annotations["Validation.Minimum"].value)
        else:
            minimum = -200.4

        if "Validation.Maximum" in annotations:
            maximum = float(annotations["Validation.Maximum"].value)
        else:
            maximum = 200.4

        var1 = (minimum + maximum) / 2
        var2 = (minimum + maximum + 1) / 2
        if var2 > maximum:
            var2 = maximum - sys.float_info.epsilon
            if var2 == int(var2):
                var2 -= sys.float_info.epsilon

        # making sure var1 has a decimal point
        if var1 == int(var1):
            temp = var1
            var1 = var2
            var2 = temp

        possible_values = [minimum, maximum, 0, '{:E}'.format(var1), var2]
        return set([i for i in possible_values if minimum <= i <= maximum])
