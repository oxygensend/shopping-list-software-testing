import {ExceptionType, ProductDto, ShoppingList, SubExceptionType} from "../../../types";
import {useForm} from "react-hook-form";
import {useEffect, useState} from "react";
import authAxios from "../../../utils/authAxios";
import {API_URL} from "../../../config";
import {Input} from "../Input";
import {findPropertyViolation} from "../../../utils/findPropertyViolations";
import {SubmitButton} from "../SubmitButton";
import {MinusPlus} from "../../MinusPlus";
import {ProductForm} from "../ProductForm";
import {Error as ErrorComponent} from "../Error";
import {mapListProductToListProductDto} from "../../../utils/mapProductToProductDto";

type ShoppingListFormProps = {
    request: (data: any) => void;
    shoppingList?: ShoppingList;
}

type FormValues = {
    name: string;
    products: ProductDto[];
    dateOfExecution: Date;
    attachmentImage: any;
    completed?: boolean;
}

export const ShoppingListForm = ({request, shoppingList}: ShoppingListFormProps) => {

    const {register, handleSubmit, reset} = useForm<FormValues>();
    const [errors, setErrors] = useState<SubExceptionType[]>([]);
    const [productNames, setProductNames] = useState<string[]>([]);
    const [grammarNames, setGrammarNames] = useState<string[]>([]);
    const [newProducts, setNewProducts] = useState<ProductDto[]>([]);
    const [productComponents, setProductComponents] = useState<any[]>([]);
    const [selectedImage, setSelectedImage] = useState<File | null>(null);

    useEffect(() => {
        authAxios.get(`${API_URL}/v1/products`)
            .then((res) => {
                setProductNames(res.data.names);
                setGrammarNames(res.data.grammarNames);

                if (shoppingList) {
                    reset({
                        name: shoppingList.name,
                        dateOfExecution: new Date(shoppingList.dateOfExecution.toString()),
                        products: shoppingList.products,
                        completed: shoppingList.completed
                    });

                    setNewProducts(mapListProductToListProductDto(shoppingList.products));
                    const components: any = [];
                    shoppingList.products.forEach((product, i) => {
                        components.push(createProductComponent(i, product.product, product.quantity, product.grammar, res.data.grammarNames));
                    });
                    setProductComponents(components);
                }
            })
    }, []);

    const onSubmit = async (body: FormValues) => {
        try {
            const form = new FormData();
            form.append('attachmentImage', body.attachmentImage[0]);
            delete body.attachmentImage
            body.products = newProducts;
            form.append('request', new Blob([JSON.stringify(body)], {type: 'application/json'}));

            console.log(form, newProducts)
            await request(form);
        } catch (err: any) {
            console.log(err)
            if (err.response.status === 400) {
                const response: ExceptionType = err.response.data;
                setErrors(response.subExceptions);
            }
        }
    }

    const renderProductForm = () => {
        const index = productComponents.length;
        setProductComponents([...productComponents,
            createProductComponent(index)]
        );
    }
    const removeProduct = (index: number) => {
        productComponents.splice(index, 1);
        setProductComponents([...productComponents])
        console.log(newProducts, productComponents)
        newProducts.splice(index, 1);
        setNewProducts([...newProducts])
    }
    const createProductComponent = (index: number, productName?: string, productQuantity?: number, productGrammar?: string, grammars?: string[]) => {
        console.log(grammarNames);
        return <ProductForm
            products={productNames}
            grammarNames={grammars ?? grammarNames}
            saveProduct={saveProduct}
            removeProduct={removeProduct}
            width={'w-1/3'}
            index={index}
            key={index}
            productVal={productName}
            quantityVal={productQuantity}
            grammarVal={productGrammar}
        />
    }

    const removeLastProductForm = () => {
        console.log(newProducts)
        const components = [...productComponents];
        components.pop();
        setProductComponents(components);
        newProducts.pop();
        setNewProducts([...newProducts]);
    }
    const saveProduct = (index: number, product: string | null, quantity: number, grammar: string) => {

        newProducts[index] = {
            name: product,
            quantity: quantity,
            grammar: grammar
        };
        console.log(newProducts);
        setNewProducts([...newProducts]);
    }




    return (
        <form
            onSubmit={handleSubmit(onSubmit)}
            className={"flex flex-col gap-2 ml-10 h-144 overflow-y-auto"}
            encType={'multipart/form-data'}
        >
            <Input
                name={'name'}
                label={'Name'}
                type={'text'}
                required={true}
                register={register}
                placeholder={'Enter name'}
                width={'w-1/3'}
                error={findPropertyViolation(errors, 'name')}
            />
            <Input
                name={'dateOfExecution'}
                label={'Date of execution'}
                type={'datetime-local'}
                required={false}
                register={register}
                placeholder={'Enter date of execution'}
                width={'w-1/3'}
                error={findPropertyViolation(errors, 'dateOfExecution')}
            />

            <Input
                name={'attachmentImage'}
                label={'Attachment Image'}
                type={'file'}
                required={false}
                register={register}
                width={'w-1/3'}
                className={"text-white"}
                error={findPropertyViolation(errors, 'attachmentImage')}
                onChange={(e) => setSelectedImage(e.target.files?.item(0) ?? null)}
                accept={'image/jpeg , image/png , image/jpg, image/webp'}
            />

            {selectedImage && <img src={URL.createObjectURL(selectedImage)} alt={'Image preview'} className={'w-44'}/>}
            {!selectedImage && shoppingList?.imageAttachmentFilename && <img
                src={`${API_URL}/v1/shopping-lists/attachment_image/${shoppingList.imageAttachmentFilename}`}
                alt={'Image preview'} className={'w-44'}
            />
            }

            {shoppingList &&
                <Input
                    name={'completed'}
                    type={'checkbox'}
                    label={'Completed'}
                    required={false}
                    register={register}
                    width={'w-1/3'}
                    className={`py-2.5 px-0  text-gray-400 border-gray-700 focus:outline-none focus:ring-0 focus:border-gray-200 w-4 h-4`}
                />
            }


            {
                findPropertyViolation(errors, 'products') &&
                <ErrorComponent error={findPropertyViolation(errors, 'products') as string}/>
            }
            {
                findPropertyViolation(errors, 'products[0].name') &&
                <ErrorComponent error={findPropertyViolation(errors, 'products[0].name') as string}/>
            }
            {
                findPropertyViolation(errors, 'products[0].quantity') &&
                <ErrorComponent error={findPropertyViolation(errors, 'products[0].quantity') as string}/>
            }

            <div>
                <p className={"text-white text-xl mt-5"}>Products:</p>
                <MinusPlus onMinusClick={removeLastProductForm} onPlusClick={renderProductForm}
                           position={'horizontal'}/>

                {productComponents.map((component) => component)}

            </div>

            <SubmitButton value={'Save'} className={'bg-blue-600 text-gray-50 rounded py-1 w-1/4  h-10 mb-3'}/>

        </form>
    )
}